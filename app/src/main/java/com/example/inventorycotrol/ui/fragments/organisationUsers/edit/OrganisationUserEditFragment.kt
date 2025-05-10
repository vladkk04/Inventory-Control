package com.example.inventorycotrol.ui.fragments.organisationUsers.edit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.example.inventorycotrol.R
import com.example.inventorycotrol.databinding.FragmentOrganisationUserEditBinding
import com.example.inventorycotrol.ui.common.base.BaseBottomSheetDialogFragment
import com.example.inventorycotrol.ui.utils.extensions.collectInLifecycle
import com.example.inventorycotrol.ui.utils.screen.InsetHandler
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrganisationUserEditFragment : BaseBottomSheetDialogFragment<FragmentOrganisationUserEditBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOrganisationUserEditBinding
        get() = FragmentOrganisationUserEditBinding::inflate

    private val viewModel: OrganisationUserEditViewModel by viewModels()

    override val isAllowFullScreen: Boolean = false

    override val isFullScreen: Boolean = false


    override fun setupViews() {
        super.setupViews()

        InsetHandler.adaptToEdgeWithPadding(binding.root)

        collectInLifecycle(viewModel.uiState) { state ->
            updateUiState(state)
        }

        bindProgressButton(binding.buttonSave)

        binding.editTextOrganisationUserName.doAfterTextChanged {
            viewModel.changeOrganisationUserName(it.toString())
        }

        binding.editTextOrganisationUserName.setText(viewModel.organisationUserNameState.value)


        binding.buttonSave.setOnClickListener {
            viewModel.saveUpdates()
        }
    }

    private fun updateUiState(uiState: OrganisationUserEditUiState) {
        if (uiState.isLoading) {
            binding.buttonSave.showProgress()
        }else{
            binding.buttonSave.hideProgress(R.string.text_save)
        }
        binding.buttonSave.isEnabled = uiState.organisationUserNameError.isNullOrBlank()
    }
}