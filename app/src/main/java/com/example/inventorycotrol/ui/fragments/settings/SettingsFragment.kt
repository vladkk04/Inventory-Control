package com.example.inventorycotrol.ui.fragments.settings
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.inventorycotrol.R
import com.example.inventorycotrol.databinding.FragmentSettingsBinding
import com.example.inventorycotrol.ui.utils.extensions.collectInLifecycle
import com.example.inventorycotrol.ui.utils.extensions.viewBinding
import com.example.inventorycotrol.ui.utils.screen.InsetHandler
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsFragment: Fragment(R.layout.fragment_settings) {

    private val binding by viewBinding(FragmentSettingsBinding::bind)

    private val viewModel: SettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        InsetHandler.adaptToEdgeWithPadding(binding.root)

        collectInLifecycle(viewModel.uiState) { state ->
            updateUiState(state)
        }
    }
    
    private fun updateUiState(uiState: SettingsUiState) {
        
    }
}