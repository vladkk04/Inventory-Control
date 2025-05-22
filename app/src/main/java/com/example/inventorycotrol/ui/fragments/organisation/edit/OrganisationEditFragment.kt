package com.example.inventorycotrol.ui.fragments.organisation.edit

import android.graphics.Bitmap
import android.icu.util.Currency
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.inventorycotrol.R
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.databinding.FragmentOrganisationEditBinding
import com.example.inventorycotrol.ui.utils.activityResultContracts.VisualMediaPicker
import com.example.inventorycotrol.ui.utils.extensions.collectInLifecycle
import com.example.inventorycotrol.ui.utils.extensions.requestImagePermissions
import com.example.inventorycotrol.ui.utils.extensions.viewBinding
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.yalantis.ucrop.UCrop
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrganisationEditFragment : Fragment(R.layout.fragment_organisation_edit) {

    private val binding by viewBinding(FragmentOrganisationEditBinding::bind)

    private val viewModel: OrganisationEditViewModel by viewModels()

    private val visualMediaPicker = VisualMediaPicker(this)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupToolbar()
        setupSaveButton()
        setupEditText()
        setupImagePicker()
        setupVisualMediaPicker()

        collectInLifecycle(viewModel.uiState) { state ->
            updateUiState(state)
        }

        collectInLifecycle(viewModel.uiFormState) { state ->
            updateUiFormState(state)
        }

        collectInLifecycle(viewModel.canSaveChanges) { canSaveChanges ->
            binding.buttonSave.isEnabled = canSaveChanges
        }

        collectInLifecycle(viewModel.organisation) { organisation ->
            organisation?.logoUrl?.let {
                binding.layoutManageImage.isGone = true
                Glide.with(requireContext())
                    .load("${AppConstants.BASE_URL_CLOUD_FRONT}${it}")
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.imageViewOrganisationLogo)
            }

            binding.editTextOrganisationName.setText(organisation?.name)
            binding.editTextDescription.setText(organisation?.description)
            binding.autoCompleteCurrency.setText(organisation?.currency, false)
        }
    }

    private fun updateUiState(uiState: OrganisationEditUiState) {
        setupAutoCompleteCurrency(uiState.currencies)

        if (uiState.isLoading){
            binding.buttonSave.showProgress()
        } else {
            binding.buttonSave.hideProgress(R.string.text_save)
        }
    }

    private fun updateUiFormState(uiFormState: OrganisationEditFormState) {
        binding.textInputOrganisationName.error = uiFormState.organisationError
    }

    private fun setupEditText() {
        binding.editTextDescription.doAfterTextChanged {
            viewModel.onEvent(OrganisationEditUiEvent.DescriptionChanged(it.toString()))
        }
        binding.editTextOrganisationName.doAfterTextChanged {
            viewModel.onEvent(OrganisationEditUiEvent.OrganisationNameChanged(it.toString()))
        }
    }

    private fun setupSaveButton() {
        bindProgressButton(binding.buttonSave)
        binding.buttonSave.setOnClickListener {
            viewModel.onEvent(OrganisationEditUiEvent.SaveChanges)
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.navigateBack()
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
                .into(binding.imageViewOrganisationLogo)

            viewModel.onEvent(OrganisationEditUiEvent.LogoChanged(it))
        }
    }

    private fun setupImagePicker() {
        binding.imageViewOrganisationLogo.setOnClickListener {
            if (viewModel.organisation.value?.logoUrl.isNullOrEmpty() && viewModel.uiFormState.value.logo == null) {
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
            binding.imageViewOrganisationLogo.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_add_image,null))
            viewModel.onEvent(OrganisationEditUiEvent.LogoChanged(null))
        }
    }

    private fun setupAutoCompleteCurrency(currencies: List<Currency>) {
        (binding.autoCompleteCurrency as? MaterialAutoCompleteTextView)?.setSimpleItems(
            currencies.map { "${it.currencyCode} - ${it.displayName}" }.toTypedArray()
        )
        binding.autoCompleteCurrency.setOnItemClickListener { _, _, i, _ ->
            viewModel.onEvent(OrganisationEditUiEvent.CurrencyChanged(currencies[i].currencyCode))
        }
    }
}