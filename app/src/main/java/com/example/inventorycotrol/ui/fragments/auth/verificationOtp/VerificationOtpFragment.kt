package com.example.inventorycotrol.ui.fragments.auth.verificationOtp

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.inventorycotrol.R
import com.example.inventorycotrol.databinding.FragmentVerificationOtpBinding
import com.example.inventorycotrol.ui.model.auth.verificationOtp.VerificationOtpUiEvent
import com.example.inventorycotrol.ui.utils.extensions.collectInLifecycle
import com.example.inventorycotrol.ui.utils.extensions.viewBinding
import com.example.inventorycotrol.ui.utils.screen.InsetHandler
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerificationOtpFragment: Fragment(R.layout.fragment_verification_otp) {

    private val binding by viewBinding(FragmentVerificationOtpBinding::bind)
    private val viewModel: VerificationOtpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        InsetHandler.adaptToEdgeWithPadding(binding.root)

        bindProgressButton(binding.buttonContinue)

        collectInLifecycle(viewModel.uiStateForm){
            binding.textInputOtp.error = it.otpError
        }

        collectInLifecycle(viewModel.uiState) {
            if (it.isLoading) {
                binding.buttonContinue.showProgress()
            } else {
                binding.buttonContinue.hideProgress(R.string.text_continue)
            }
        }

        binding.editTextOtp.doAfterTextChanged {
            viewModel.onEvent(VerificationOtpUiEvent.OtpChanged(it.toString()))
        }

        binding.textResend.setOnClickListener {
            viewModel.onEvent(VerificationOtpUiEvent.ResendOtp)
        }

        binding.buttonBack.setOnClickListener {
            viewModel.onEvent(VerificationOtpUiEvent.NavigateBack)
        }

        binding.buttonContinue.setOnClickListener {
            viewModel.onEvent(VerificationOtpUiEvent.Continue)
        }
    }
}