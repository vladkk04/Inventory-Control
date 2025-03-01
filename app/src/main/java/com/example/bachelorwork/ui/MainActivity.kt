package com.example.bachelorwork.ui

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import com.example.bachelorwork.databinding.ActivityMainBinding
import com.example.bachelorwork.ui.navigation.AppNavigationGraph
import com.example.bachelorwork.ui.navigation.AppNavigator
import com.example.bachelorwork.ui.navigation.setupWithNavController
import com.example.bachelorwork.ui.snackbar.SnackbarController
import com.example.bachelorwork.ui.utils.extensions.viewBinding
import com.example.bachelorwork.ui.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    private val viewModel: MainViewModel by viewModels()

    @Inject lateinit var appNavigator: AppNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        setupAppSnackbarObserver()
        setupAppNavigation()
        setupAppPermissions()

        setupCallBackOnBackPressed()
    }

    private fun setupAppNavigation() {
        val navHostFragment: NavHostFragment = binding.navHostFragment.getFragment() as NavHostFragment
        val navController = navHostFragment.navController

        AppNavigationGraph(this, appNavigator, navController, binding.drawerLayout)

        binding.bottomNavigationView.setupWithNavController(navController)
        binding.navigationView.setupWithNavController(navController, binding.drawerLayout)
    }

    private fun setupAppSnackbarObserver() {
        SnackbarController.observeSnackbarEvents(this, binding.root)
    }

    private fun setupAppPermissions() {
        viewModel.permissionController.registerForActivityResult(this)
    }

    private fun setupCallBackOnBackPressed() {
        val callback = object: OnBackPressedCallback(true) {
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
            if (viewModel.onBackPressed()) { finish() }
        }
    }
}