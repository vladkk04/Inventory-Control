package com.example.bachelorwork.ui

import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.bachelorwork.R
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

    private val navHostFragment: NavHostFragment by lazy { supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment }
    private val navController: NavController by lazy { navHostFragment.navController }

    @Inject lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater).apply { setContentView(root) }

        enableEdgeToEdge()
        setupOnBackPressedCallback()

        NavigationGraph(this, navigator, navController)
        SnackbarController.observeSnackbarEvents(this, binding.root)

        binding.bottomNavigationView.setupWithNavController(navController, binding.navHostFragment)

    }

    private fun setupOnBackPressedCallback() {
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