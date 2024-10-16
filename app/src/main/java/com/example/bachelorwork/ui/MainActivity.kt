package com.example.bachelorwork.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.bachelorwork.databinding.ActivityMainBinding
import com.example.bachelorwork.ui.productList.ProductListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        _binding = ActivityMainBinding.inflate(layoutInflater).apply { setContentView(root) }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                replace(binding.fragmentContainerView.id, ProductListFragment())
                setReorderingAllowed(true)
            }.commit()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}