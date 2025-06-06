package com.example.inventorycotrol.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.elveum.elementadapter.simpleAdapter
import com.example.inventorycotrol.R
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.databinding.ActivityMainBinding
import com.example.inventorycotrol.databinding.HeaderNavigationDrawerBinding
import com.example.inventorycotrol.databinding.OrganisationItemBinding
import com.example.inventorycotrol.domain.model.organisation.OrganisationRole
import com.example.inventorycotrol.ui.model.organisation.OrganisationItem
import com.example.inventorycotrol.ui.navigation.AppNavigationGraph
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.navigation.setupWithNavController
import com.example.inventorycotrol.ui.snackbar.SnackbarController
import com.example.inventorycotrol.ui.utils.screen.InsetHandler
import com.google.android.material.internal.NavigationMenuView
import com.google.android.material.navigation.NavigationView
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var bindingNavigationHeader: HeaderNavigationDrawerBinding

    private val viewModel: MainViewModel by viewModels()

    private var isAppLaunchedAtFirst = false

    @Inject
    lateinit var appNavigator: AppNavigator

    private val adapter = simpleAdapter<OrganisationItem, OrganisationItemBinding> {
        areItemsSame = { oldItem, newItem -> oldItem.id == newItem.id }
        areContentsSame = { oldItem, newItem -> oldItem == newItem }

        bind { item ->
            this.selectedOrganisation.isVisible = item.isSelected
            this.organisationName.text = item.name
        }

        listeners {
            this.root.onClick { item ->
                viewModel.switchOrganisation(item.id)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        setupApp()

        lifecycleScope.launch {
            viewModel.organisationRole.collectLatest {
                when (it) {
                    OrganisationRole.ADMIN -> {
                        binding.bottomNavigationView.menu.findItem(R.id.orders).isVisible = true
                        binding.bottomNavigationView.menu.findItem(R.id.more).isVisible = true
                        binding.navigationView.menu.findItem(R.id.reports).isVisible = true
                    }

                    OrganisationRole.EMPLOYEE -> {
                        binding.bottomNavigationView.menu.findItem(R.id.more).isVisible = true
                        binding.bottomNavigationView.menu.findItem(R.id.orders).isVisible = false
                        binding.navigationView.menu.findItem(R.id.reports).isVisible = false
                    }

                    null -> {}
                }
            }
        }

        lifecycleScope.launch {
            viewModel.uiState.collectLatest { uiState ->
                uiState.profile?.let {
                    with(bindingNavigationHeader) {
                        Glide.with(this@MainActivity)
                            .load("${AppConstants.BASE_URL_CLOUD_FRONT}${it.imageUrl}")
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

        lifecycleScope.launch {
            viewModel.isConnected.collectLatest { isConnected ->
                bindingNavigationHeader.checkBoxShowOrganisations.isEnabled = isConnected
                bindingNavigationHeader.checkBoxShowOrganisations.isChecked = isConnected
                bindingNavigationHeader.recyclerView.isGone = !isConnected
                bindingNavigationHeader.divider.isGone = !isConnected
                bindingNavigationHeader.createOrganisation.isGone = !isConnected
                bindingNavigationHeader.dividerCreateOrganisation.isGone = !isConnected
                if (!isAppLaunchedAtFirst) {
                    isAppLaunchedAtFirst = true
                    return@collectLatest
                }
                when (isConnected) {
                    true -> {
                        if (binding.textViewInternetConnection.isGone) {
                            return@collectLatest
                        }
                        binding.textViewInternetConnection.apply {
                            isGone = false
                            text = getString(R.string.internet_connection)
                            backgroundTintList =
                                ColorStateList.valueOf(getColor(R.color.colorMinStockLevelNormalLevel))
                        }
                        binding.textViewInternetConnection.animate()
                            .setStartDelay(1000)
                            .setDuration(400)
                            .alpha(0f)
                            .withEndAction {
                                binding.textViewInternetConnection.isGone = true
                                binding.textViewInternetConnection.alpha = 1f
                            }
                            .start()
                    }

                    false -> {
                        binding.textViewInternetConnection.apply {
                            isGone = false
                            text = getString(R.string.no_internet_connection)
                            backgroundTintList =
                                ColorStateList.valueOf(getColor(R.color.colorMinStockLevelLowLevel))
                        }
                    }
                }

            }



        }
    }

    private fun setupApp() {
        enableEdgeToEdge()
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
        }.request { _, _, _-> }
    }

    private fun setupNavigationDrawerView() {
        bindingNavigationHeader =
            HeaderNavigationDrawerBinding.bind(binding.navigationView.getHeaderView(0))

        InsetHandler.adaptToEdgeWithMargin(bindingNavigationHeader.root)

        disableNavigationViewScrollbars(binding.navigationView)

        bindingNavigationHeader.createOrganisation.setOnClickListener {
            viewModel.navigateToCreateOrganisation()
        }

        bindingNavigationHeader.imageViewUserLogo.setOnClickListener {
            viewModel.navigateToProfile()
        }

        bindingNavigationHeader.checkBoxShowOrganisations.setOnCheckedChangeListener { b, isChecked ->
            bindingNavigationHeader.recyclerView.isGone = !isChecked
            bindingNavigationHeader.divider.isGone = !isChecked
            bindingNavigationHeader.createOrganisation.isGone = !isChecked
            bindingNavigationHeader.dividerCreateOrganisation.isGone = !isChecked
        }

        bindingNavigationHeader.recyclerView.apply {
            adapter = this@MainActivity.adapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }


    @SuppressLint("RestrictedApi")
    private fun disableNavigationViewScrollbars(navigationView: NavigationView?) {
        if (navigationView != null) {
            val navigationMenuView = navigationView.getChildAt(0) as NavigationMenuView
            navigationMenuView.isVerticalScrollBarEnabled = false
        }
    }
}