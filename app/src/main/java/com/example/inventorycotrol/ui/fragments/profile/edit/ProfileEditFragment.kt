package com.example.inventorycotrol.ui.fragments.profile.edit

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.inventorycotrol.R
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.databinding.FragmentProfileEditBinding
import com.example.inventorycotrol.ui.utils.activityResultContracts.VisualMediaPicker
import com.example.inventorycotrol.ui.utils.extensions.collectInLifecycle
import com.example.inventorycotrol.ui.utils.extensions.requestImagePermissions
import com.example.inventorycotrol.ui.utils.extensions.viewBinding
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.yalantis.ucrop.UCrop
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileEditFragment : Fragment(R.layout.fragment_profile_edit) {

    private val binding by viewBinding(FragmentProfileEditBinding::bind)

    private val viewModel: ProfileEditViewModel by viewModels()

    private val visualMediaPicker = VisualMediaPicker(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()
        setupVisualMediaPicker()

        collectInLifecycle(viewModel.uiState) { state ->
            updateUiState(state)
        }

        collectInLifecycle(viewModel.canSaveChanges) { state ->
            binding.buttonSave.isEnabled = state
        }

        bindProgressButton(binding.buttonSave)

        binding.imageViewProfileLogo.setOnClickListener {
            requestImagePermissions {
                visualMediaPicker.launchVisualMediaPicker()
            }
        }

        binding.buttonSave.setOnClickListener {
            viewModel.onEvent(ProfileEditUiEvent.SaveChanges)
        }

        binding.editTextFullName.doAfterTextChanged {
            viewModel.onEvent(ProfileEditUiEvent.FullNameChanged(it.toString()))
        }
    }


    private fun updateUiState(uiState: ProfileEditUiState) {
        if (uiState.isLoading) {
            binding.buttonSave.showProgress()
        } else {
            binding.buttonSave.hideProgress(R.string.text_save)
        }
        binding.editTextFullName.setText(uiState.fullName)

        Glide.with(requireContext())
            .load("${AppConstants.BASE_URL_CLOUD_FRONT}/${uiState.logoUrl}")
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.imageViewProfileLogo)
    }

    private fun setupVisualMediaPicker() {
        visualMediaPicker.setupUCropOptions(UCrop.Options().apply {
            withAspectRatio(1f, 1f)
            withMaxResultSize(768, 768)
            setCompressionFormat(Bitmap.CompressFormat.JPEG)
            setCompressionQuality(80)
        })

        visualMediaPicker.addCallbackResult {
            Glide.with(requireContext())
                .load(it)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.imageViewProfileLogo)

            viewModel.onEvent(ProfileEditUiEvent.LogoChanged(it))
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.navigateBack()
        }
    }
}