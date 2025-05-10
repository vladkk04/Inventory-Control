package com.example.inventorycotrol.ui.fragments.organisation.manage.create

import android.graphics.Bitmap
import android.icu.util.Currency
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.inventorycotrol.R
import com.example.inventorycotrol.databinding.FragmentCreateOrganisationBinding
import com.example.inventorycotrol.ui.model.organisation.CreateOrganisationUiEvent
import com.example.inventorycotrol.ui.utils.activityResultContracts.VisualMediaPicker
import com.example.inventorycotrol.ui.utils.extensions.collectInLifecycle
import com.example.inventorycotrol.ui.utils.extensions.requestImagePermissions
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.yalantis.ucrop.UCrop
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateOrganisationFragment : Fragment() {

    private var _binding: FragmentCreateOrganisationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateOrganisationViewModel by viewModels()

    private val visualMediaPicker = VisualMediaPicker(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateOrganisationBinding.inflate(inflater, container, false)

        setupEditsText()
        setupImagePicker()
        setupVisualMediaPicker()

        binding.toolbar.setNavigationOnClickListener {
            viewModel.navigateBack()
        }

        bindProgressButton(binding.buttonCreateOrganisation)

        collectInLifecycle(viewModel.uiState) { state ->
            if (state.isLoading) {
                binding.buttonCreateOrganisation.showProgress()
            } else {
                binding.buttonCreateOrganisation.hideProgress(R.string.text_create_organisation)
            }
            setupAutoCompleteCurrency(state.currencies)
        }

        collectInLifecycle(viewModel.uiStateFrom) { state ->
            binding.textInputOrganisation.error = state.organisationError
            binding.textInputCurrency.error = state.currencyError

            binding.autoCompleteCurrency.setText(state.currency, false)
        }


        return binding.root
    }

    private fun setupVisualMediaPicker() {
        visualMediaPicker.setupUCropOptions(UCrop.Options().apply {
            withAspectRatio(1f, 1f)
            withMaxResultSize(768, 768)
            setCompressionFormat(Bitmap.CompressFormat.JPEG)
            setCompressionQuality(80)
        })

        visualMediaPicker.addCallbackResult {
            it?.let { uri ->
                Glide.with(requireContext())
                    .load(uri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .error(R.drawable.ic_add_image)
                    .centerCrop()
                    .into(binding.imageViewOrganisationLogo)

                viewModel.onEvent(CreateOrganisationUiEvent.LogoChanged(uri))
            }


        }
    }

    private fun setupImagePicker() {
        binding.imageViewOrganisationLogo.setOnClickListener {
            requestImagePermissions {
                visualMediaPicker.launchVisualMediaPicker()
            }
        }
    }

    private fun setupEditsText() {
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