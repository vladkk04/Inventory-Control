package com.example.bachelorwork.ui.fragments.organisation.manage.create

import android.graphics.Bitmap
import android.icu.util.Currency
import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentCreateOrganisationBinding
import com.example.bachelorwork.ui.model.organisation.CreateOrganisationUiEvent
import com.example.bachelorwork.ui.utils.activityResultContracts.VisualMediaPicker
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import com.example.bachelorwork.ui.utils.extensions.requestImagePermissions
import com.example.bachelorwork.ui.utils.extensions.viewBinding
import com.example.bachelorwork.ui.utils.screen.InsetHandler
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.yalantis.ucrop.UCrop
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateOrganisationFragment : Fragment(R.layout.fragment_create_organisation) {

    private val binding by viewBinding(FragmentCreateOrganisationBinding::bind)
    private val viewModel: CreateOrganisationViewModel by viewModels()

    private val visualMediaPicker = VisualMediaPicker(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        InsetHandler.adaptToEdgeWithMargin(binding.root)

        visualMediaPicker.setupUCropOptions(UCrop.Options().apply {
            withAspectRatio(1f, 1f)
            withMaxResultSize(768, 768)
            setCompressionFormat(Bitmap.CompressFormat.JPEG)
            setCompressionQuality(80)
        })

        collectInLifecycle(viewModel.uiState) { state ->
            setupAutoCompleteCurrency(state.currencies)
        }

        collectInLifecycle(viewModel.uiStateFrom) { state ->
            binding.textInputOrganisation.error = state.organisationError
            binding.textInputCurrency.error = state.currencyError

            binding.autoCompleteCurrency.setText(state.currency, false)
        }

        visualMediaPicker.addCallbackResult { uri ->

            uri?.let {
                Glide.with(this)
                    .load(it)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .into(binding.imageViewOrganisationLogo)
            }

            viewModel.onEvent(CreateOrganisationUiEvent.LogoChanged(uri))
        }

        bindProgressButton(binding.buttonCreateOrganisation)

        collectInLifecycle(viewModel.uiState) { state ->
            if (state.isLoading) {
                binding.buttonCreateOrganisation.showProgress()
            } else {
                binding.buttonCreateOrganisation.hideProgress(R.string.text_create_organisation)
            }
        }

        binding.imageViewOrganisationLogo.setOnClickListener {
            requestImagePermissions {
                visualMediaPicker.launchVisualMediaPicker()
            }
        }

        binding.buttonCreateOrganisation.setOnClickListener {
            viewModel.onEvent(CreateOrganisationUiEvent.Create)
        }

        binding.editTextOrganisation.doAfterTextChanged {
            viewModel.onEvent(CreateOrganisationUiEvent.OrganisationNameChanged(it.toString()))
        }

        binding.editTextDescription.doAfterTextChanged {
            viewModel.onEvent(CreateOrganisationUiEvent.DescriptionChanged(it.toString()))
        }
    }

    private fun setupAutoCompleteCurrency(currencies: List<Currency>) {
        (binding.autoCompleteCurrency as? MaterialAutoCompleteTextView)?.setSimpleItems(
            currencies.map { "${it.currencyCode} - ${it.displayName}" }.toTypedArray()
        )
        binding.autoCompleteCurrency.setOnItemClickListener { _, _, i, _ ->
            viewModel.onEvent(CreateOrganisationUiEvent.CurrencyChanged(currencies[i].currencyCode))
        }
    }
}