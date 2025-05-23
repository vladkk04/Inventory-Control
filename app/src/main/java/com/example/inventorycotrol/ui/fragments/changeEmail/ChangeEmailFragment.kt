package com.example.inventorycotrol.ui.fragments.changeEmail

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.inventorycotrol.R
import com.example.inventorycotrol.databinding.FragmentChangeEmailBinding
import com.example.inventorycotrol.ui.MainViewModel
import com.example.inventorycotrol.ui.utils.extensions.collectInLifecycle
import com.example.inventorycotrol.ui.utils.extensions.viewBinding
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ChangeEmailFragment : Fragment(R.layout.fragment_change_email) {

    private val binding by viewBinding(FragmentChangeEmailBinding::bind)

    private val viewModel: ChangeEmailViewModel by viewModels()

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindProgressButton(binding.buttonChangeEmail)

        binding.buttonChangeEmail.isEnabled = false

        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.isConnected.collectLatest {
                binding.buttonChangeEmail.isEnabled = it
            }
        }

        collectInLifecycle(viewModel.uiState) { state ->
            updateUiState(state)
        }

        collectInLifecycle(viewModel.emailError) { error ->
            binding.buttonChangeEmail.isEnabled = error.isNullOrEmpty() && binding.editTextEmail.text.toString().isNotEmpty()
        }

        binding.toolbar.setNavigationOnClickListener {
            viewModel.navigateBack()
        }

        binding.editTextEmail.doAfterTextChanged {
            viewModel.setupChangeEmail(it.toString())
        }

        binding.buttonChangeEmail.setOnClickListener {
            viewModel.changeEmail()
        }
    }

    private fun updateUiState(uiState: ChangeEmailUiState) {
        if (uiState.isLoading) {
            binding.buttonChangeEmail.showProgress()
        } else {
            binding.buttonChangeEmail.hideProgress(R.string.text_change_email)
        }
    }
}