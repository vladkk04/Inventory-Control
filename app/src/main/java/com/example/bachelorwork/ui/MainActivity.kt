package com.example.bachelorwork.ui

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.bachelorwork.databinding.ActivityMainBinding
import com.example.bachelorwork.ui.navigation.NavigationGraph
import com.example.bachelorwork.ui.navigation.Navigator
import com.example.bachelorwork.ui.navigation.setupWithNavController
import com.example.bachelorwork.ui.utils.snackbar.SnackbarController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    private val navHostFragment: NavHostFragment by lazy { binding.navHostFragment.getFragment() as NavHostFragment }
    private val navController: NavController by lazy { navHostFragment.navController }

    @Inject lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater).apply { setContentView(root) }
        setupCallBackOnBackPressed()
        enableEdgeToEdge()

        NavigationGraph(this, navigator, navController, binding.drawerLayout)
        SnackbarController.observeSnackbarEvents(this, binding.root)

        binding.bottomNavigationView.setupWithNavController(navController)
        binding.navigationView.setupWithNavController(navController)
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


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}