package com.example.bachelorwork.ui.fragments.changeEmail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentChangeEmailBinding
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import com.example.bachelorwork.ui.utils.extensions.viewBinding
import com.example.bachelorwork.ui.utils.screen.InsetHandler
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChangeEmailFragment : Fragment(R.layout.fragment_change_email) {

    private val binding by viewBinding(FragmentChangeEmailBinding::bind)

    private val viewModel: ChangeEmailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        InsetHandler.adaptToEdgeWithPadding(binding.root)

        collectInLifecycle(viewModel.uiState) { state ->
            updateUiState(state)
        }
    }

    private fun updateUiState(uiState: ChangeEmailUiState) {

    }
}