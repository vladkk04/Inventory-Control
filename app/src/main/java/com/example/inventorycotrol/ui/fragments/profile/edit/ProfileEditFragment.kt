package com.example.inventorycotrol.ui.fragments.profile.edit

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.inventorycotrol.R
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.databinding.FragmentProfileEditBinding
import com.example.inventorycotrol.ui.MainViewModel
import com.example.inventorycotrol.ui.utils.activityResultContracts.VisualMediaPicker
import com.example.inventorycotrol.ui.utils.extensions.collectInLifecycle
import com.example.inventorycotrol.ui.utils.extensions.requestImagePermissions
import com.example.inventorycotrol.ui.utils.extensions.viewBinding
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.yalantis.ucrop.UCrop
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileEditFragment : Fragment(R.layout.fragment_profile_edit) {

    private val binding by viewBinding(FragmentProfileEditBinding::bind)

    private val viewModel: ProfileEditViewModel by viewModels()

    private val mainViewModel: MainViewModel by activityViewModels()

    private val visualMediaPicker = VisualMediaPicker(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()
        setupVisualMediaPicker()

        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.isConnected.collectLatest {
                binding.buttonSave.isEnabled = it
            }
        }

        collectInLifecycle(viewModel.uiState) { state ->
            updateUiState(state)
        }

        collectInLifecycle(viewModel.canSaveChanges) { state ->
            binding.buttonSave.isEnabled = state && mainViewModel.isConnected.value
        }

        bindProgressButton(binding.buttonSave)

        binding.imageViewProfileLogo.setOnClickListener {
            if (viewModel.uiState.value.logoUrl.isNullOrEmpty() || viewModel.uiFormState.value.logo == null) {
                requestImagePermissions {
                    visualMediaPicker.launchVisualMediaPicker()
                }
            } else {
                binding.layoutManageImage.isGone = false
            }
        }
        binding.buttonEdit.setOnClickListener {
            requestImagePermissions {
                visualMediaPicker.launchVisualMediaPicker()
            }
        }
        binding.buttonDelete.setOnClickListener {
            binding.layoutManageImage.isGone = true
            binding.imageViewProfileLogo.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_add_image,null))
            viewModel.onEvent(ProfileEditUiEvent.LogoChanged(null))
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

        uiState.logoUrl?.let {
            Glide.with(requireContext())
                .load("${AppConstants.BASE_URL_CLOUD_FRONT}${it}")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.imageViewProfileLogo)
        }

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

            binding.layoutManageImage.isGone = true

            viewModel.onEvent(ProfileEditUiEvent.LogoChanged(it))
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.navigateBack()
        }
    }
}