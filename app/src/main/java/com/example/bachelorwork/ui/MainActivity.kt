package com.example.bachelorwork.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeActivity
import com.elveum.elementadapter.simpleAdapter
import com.example.bachelorwork.R
import com.example.bachelorwork.data.constants.AppConstants
import com.example.bachelorwork.databinding.ActivityMainBinding
import com.example.bachelorwork.databinding.HeaderNavigationDrawerBinding
import com.example.bachelorwork.databinding.OrganisationItemBinding
import com.example.bachelorwork.domain.model.organisation.Organisation
import com.example.bachelorwork.theme.CustomAppTheme
import com.example.bachelorwork.theme.DarkTheme
import com.example.bachelorwork.ui.navigation.AppNavigationGraph
import com.example.bachelorwork.ui.navigation.AppNavigator
import com.example.bachelorwork.ui.navigation.setupWithNavController
import com.example.bachelorwork.ui.snackbar.SnackbarController
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import com.example.bachelorwork.ui.utils.hideKeyboard
import com.example.bachelorwork.ui.worker.StockCheckWorker
import com.google.android.material.internal.NavigationMenuView
import com.google.android.material.navigation.NavigationView
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ThemeActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var bindingNavigationHeader: HeaderNavigationDrawerBinding

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var appNavigator: AppNavigator

    private val adapter = simpleAdapter<Organisation, OrganisationItemBinding> {

        bind { item ->
            this.selectedOrganisation.isVisible = viewModel.uiState.value.selectedOrganisationId == item.id
            this.organisationName.text = item.name
        }

        listeners {
            this.organisationName.onClick { item ->
                viewModel.switchOrganisation(item.name)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        setupApp()

        collectInLifecycle(viewModel.uiState) { uiState ->
            uiState.profile?.let {
                with(bindingNavigationHeader) {
                    Glide.with(this@MainActivity)
                        .load("${AppConstants.BASE_URL}${it.imageUrl}")
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ic_profile_outline)
                        .fallback(R.drawable.ic_profile_outline)
                        .error(R.drawable.ic_profile_outline)
                        .centerCrop()
                        .into(imageViewUserLogo)

                    textViewFullName.text = it.fullName
                    textViewOrganisationRole.text = it.organisationRole.name
                }
            }

            adapter.submitList(uiState.organisations)
        }

    }

    private fun setupApp() {
        enableEdgeToEdge()
        scheduleStockCheckWorker()
        setupAppNavigation()
        setupNavigationDrawerView()
        setupAppSnackbarObserver()
        setupCallBackOnBackPressed()
        requestPermissionsOnLaunchApp()
    }

    private fun setupAppNavigation() {
        val navHostFragment: NavHostFragment =
            binding.navHostFragment.getFragment() as NavHostFragment
        val navController = navHostFragment.navController

        AppNavigationGraph(this, appNavigator, navController, drawerLayout = binding.root)

        binding.bottomNavigationView.setupWithNavController(navController)
        binding.navigationView.setupWithNavController(navController, binding.root)
    }

    private fun setupAppSnackbarObserver() {
        SnackbarController.observeSnackbarEvents(this, binding.root)
    }

    private fun setupCallBackOnBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                binding.drawerLayout.close()
            }
        }

        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerOpened(drawerView: View) {
                onBackPressedDispatcher.addCallback(this@MainActivity, callback)
                hideKeyboard()
            }

            override fun onDrawerClosed(drawerView: View) {
                callback.remove()
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) = Unit
            override fun onDrawerStateChanged(newState: Int) = Unit
        })

        onBackPressedDispatcher.addCallback {
            if (viewModel.onBackPressed()) {
                finish()
            }
        }
    }

    private fun requestPermissionsOnLaunchApp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionX.init(this).permissions(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            PermissionX.init(this).permissions(PermissionX.permission.POST_NOTIFICATIONS)
        }.onExplainRequestReason { scope, deniedList ->
            scope.showRequestReasonDialog(
                deniedList,
                "Core fundamental are based on these permissions",
                "Allow",
                "Deny"
            )
        }.onForwardToSettings { scope, deniedList ->
            scope.showForwardToSettingsDialog(
                deniedList,
                "You need to allow necessary permissions in Settings manually",
                "Allow",
                "Deny"
            )
        }.request { allGranted, grantedList, deniedList ->
            if (allGranted) {
                Toast.makeText(this, "All permissions are granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(
                    this,
                    "These permissions are denied: $deniedList",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setupNavigationDrawerView() {
        bindingNavigationHeader =
            HeaderNavigationDrawerBinding.bind(binding.navigationView.getHeaderView(0))

        disableNavigationViewScrollbars(binding.navigationView)

        /*bindingNavigationHeader.switchThemeMode.setOnCheckedChangeListener { b, _ ->
            when (ThemeManager.instance.getCurrentTheme()?.id()) {
                0 -> ThemeManager.instance.changeTheme(LightTheme(this), b)
                1 -> ThemeManager.instance.changeTheme(DarkTheme(this), b)
            }
        }*/

        bindingNavigationHeader.imageViewUserLogo.setOnClickListener {
            viewModel.navigateToProfile()
        }

        bindingNavigationHeader.checkBoxShowOrganisations.setOnCheckedChangeListener { b, isChecked ->
            bindingNavigationHeader.recyclerView.isGone = !isChecked
            bindingNavigationHeader.divider.isGone = !isChecked
        }

        bindingNavigationHeader.recyclerView.apply {
            adapter = this@MainActivity.adapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    override fun getStartTheme(): AppTheme = DarkTheme(this)

    override fun syncTheme(appTheme: AppTheme) {
        val darkTheme = appTheme as CustomAppTheme

        binding.navigationView.setBackgroundColor(darkTheme.colorBackground())
        bindingNavigationHeader.cardView.setCardBackgroundColor(darkTheme.colorBackground())
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

       /* when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                ThemeManager.instance.changeTheme(
                    LightTheme(this),
                    bindingNavigationHeader.switchThemeMode
                )
            }

            Configuration.UI_MODE_NIGHT_YES -> {
                ThemeManager.instance.changeTheme(
                    DarkTheme(this),
                    bindingNavigationHeader.switchThemeMode
                )
            }
        }*/
    }

    private fun scheduleStockCheckWorker() {

        val workManager = WorkManager.getInstance(this)

        workManager.cancelUniqueWork("stockCheck")

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<StockCheckWorker>()
            .setInitialDelay(Duration.ofSeconds(10))
            .setBackoffCriteria(backoffPolicy = BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
            .setConstraints(constraints).build()

        workManager.enqueueUniqueWork("stockCheck", ExistingWorkPolicy.KEEP, request)
    }


    @SuppressLint("RestrictedApi")
    private fun disableNavigationViewScrollbars(navigationView: NavigationView?) {
        if (navigationView != null) {
            val navigationMenuView = navigationView.getChildAt(0) as NavigationMenuView
            navigationMenuView.isVerticalScrollBarEnabled = false
        }
    }
}