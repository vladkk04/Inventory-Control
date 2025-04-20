package com.example.bachelorwork.ui.model.auth.verificationOtp

sealed class VerificationOtpUiEvent {
    data class OtpChanged(val otp: String) : VerificationOtpUiEvent()
    data object NavigateBack : VerificationOtpUiEvent()
    data object ResendOtp : VerificationOtpUiEvent()
    data object Continue : VerificationOtpUiEvent()
}