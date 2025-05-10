package com.example.inventorycotrol.ui.fragments.profile
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.inventorycotrol.R
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.databinding.FragmentProfileBinding
import com.example.inventorycotrol.ui.common.AppDialogs
import com.example.inventorycotrol.ui.utils.extensions.collectInLifecycle
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment: Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        setupButtons()
        setupToolbar()

        collectInLifecycle(viewModel.uiState) { state ->
            updateUiState(state)
        }

        return binding.root
    }
    
    private fun updateUiState(uiState: ProfileUiState) {

        uiState.logoUrl?.let {
            Glide.with(requireContext())
                .load("${AppConstants.BASE_URL_CLOUD_FRONT}/${it}")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.ic_profile_outline)
                .skipMemoryCache(true)
                .into(binding.imageViewAvatar)
        }

        binding.textViewId.text = getString(R.string.text_your_id, uiState.id)
        binding.textViewFullName.text = uiState.fullName
        binding.textViewEmail.text = uiState.email
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.navigateUp()
        }
        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.edit -> {
                    viewModel.navigateToProfileEdit()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupButtons() {
        binding.buttonSignOut.setOnClickListener {
            AppDialogs.createSignOutDialog(requireContext()) {
                viewModel.signOut()
            }.show()
        }

        binding.buttonChangeEmail.setOnClickListener {
            viewModel.navigateToChangeEmail()
        }

        binding.buttonChangePassword.setOnClickListener {
            viewModel.navigateToChangePassword()
        }
    }
}