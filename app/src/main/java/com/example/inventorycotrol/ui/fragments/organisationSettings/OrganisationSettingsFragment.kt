package com.example.inventorycotrol.ui.fragments.organisationSettings

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.inventorycotrol.R
import com.example.inventorycotrol.databinding.FragmentOrganisationSettingsBinding
import com.example.inventorycotrol.domain.model.organisation.OrganisationRole
import com.example.inventorycotrol.ui.common.AppDialogs
import com.example.inventorycotrol.ui.utils.extensions.collectInLifecycle
import com.example.inventorycotrol.ui.utils.extensions.viewBinding
import com.example.inventorycotrol.ui.views.createOrganisationRoleChip
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Locale


@AndroidEntryPoint
class OrganisationSettingsFragment : Fragment(R.layout.fragment_organisation_settings) {

    private val binding by viewBinding(FragmentOrganisationSettingsBinding::bind)

    private val viewModel: OrganisationSettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupToolbar()
        setupTimePicker()
        setupChipGroup()
        setupToggleButton()
        setupNotifyBy()

        setupEditText()

        binding.buttonSave.setOnClickListener {
            viewModel.saveSettings()
        }

        collectInLifecycle(viewModel.uiState) { state ->
            updateUiState(state)
        }
    }

    private fun updateUiState(uiState: OrganisationSettingsUiState) {
        binding.editTextNormalThreshold.setText(
            String.format(
                Locale.getDefault(),
                "%.2f",
                uiState.thresholdSettings.normalThresholdPercentage
            )
        )
        binding.editTextMediumThreshold.setText(
            String.format(
                Locale.getDefault(),
                "%.2f",
                uiState.thresholdSettings.mediumThresholdPercentage
            )
        )
        binding.editTextCriticalThreshold.setText(
            String.format(
                Locale.getDefault(),
                "%.2f",
                uiState.thresholdSettings.criticalThresholdPercentage
            )
        )

        uiState.notificationSettings.notifiableRoles.forEach {
            val chip = binding.chipGroupOrganisationRole.getChildAt(it.ordinal)
            if (chip is Chip) {
                chip.isChecked = true
            }
        }

        binding.textViewAtEveryDay.text = getString(
            R.string.text_at_time_value,
            uiState.notificationSettings.notificationTime
        )

        binding.textViewAtSpecificDays.text = getString(
            R.string.text_at_time_value,
            uiState.notificationSettings.notificationTime
        )

        uiState.notificationSettings.notificationDays.forEach {
            val getButtonDay = binding.toggleButton.getChildAt(it - 1)
            binding.toggleButton.check(getButtonDay.id)
        }

        if (uiState.notificationSettings.notificationDays.size == 7) {
            binding.radioButtonEveryDay.isChecked = true
        } else {
            binding.radioButtonSpecificDays.isChecked = true
        }
    }

    private fun setupNotifyBy() {
        binding.chipGroupOrganisationRole.setOnCheckedStateChangeListener { group, checkedIds ->
            val selectedRoles = mutableSetOf<OrganisationRole>()
            for (i in 0 until group.childCount) {
                val chip = group.getChildAt(i) as Chip
                if (checkedIds.contains(chip.id)) {
                    selectedRoles.add(OrganisationRole.valueOf(chip.text.toString()))
                }
            }
            viewModel.updateNotifiableRoles(selectedRoles)
        }
    }

    private fun setupEditText() {
        binding.editTextCriticalThreshold.doAfterTextChanged {
            viewModel.updateCriticalThreshold(it.toString().toDoubleOrNull() ?: 0.00)
        }
        binding.editTextMediumThreshold.doAfterTextChanged {
            viewModel.updateMediumThreshold(it.toString().toDoubleOrNull() ?: 0.00)
        }
        binding.editTextNormalThreshold.doAfterTextChanged {
            viewModel.updateNormalThreshold(it.toString().toDoubleOrNull() ?: 0.00)
        }
    }

    private fun setupToggleButton() {
        binding.toggleButton.addOnButtonCheckedListener { group, checkedId, isChecked ->
            val selectedDays = mutableSetOf<Int>()

            for (i in 0 until group.childCount) {
                val button = group.getChildAt(i)
                if (group.checkedButtonIds.contains(button.id)) {
                    selectedDays.add(i + 1)
                }
            }

            viewModel.updateNotificationDays(selectedDays)
        }
    }

    private fun setupChipGroup() {
        binding.radioButtonEveryDay.setOnCheckedChangeListener { compoundButton, b ->
            binding.radioButtonSpecificDays.isChecked = !b
            binding.toggleButton.isGone = b
            binding.textViewAtEveryDay.isVisible = b
            if (b) {
                viewModel.updateNotificationDays(
                    setOf(
                        Calendar.MONDAY,
                        Calendar.TUESDAY,
                        Calendar.WEDNESDAY,
                        Calendar.THURSDAY,
                        Calendar.FRIDAY,
                        Calendar.SATURDAY,
                        Calendar.SUNDAY
                    )
                )
            }
        }

        binding.radioButtonSpecificDays.setOnCheckedChangeListener { compoundButton, b ->
            binding.radioButtonEveryDay.isChecked = !b
            binding.toggleButton.isVisible = b
            binding.textViewAtSpecificDays.isVisible = b
        }

        OrganisationRole.entries.map { createOrganisationRoleChip(requireContext(), it) }
            .forEach { chip ->
                binding.chipGroupOrganisationRole.addView(chip)
            }
    }

    private fun setupTimePicker() {
        binding.textViewAtEveryDay.setOnClickListener {
            val picker = AppDialogs.createTimePicker()

            picker.addOnPositiveButtonClickListener {
                binding.textViewAtEveryDay.text =
                    String.format(Locale.getDefault(), "at %02d:%02d", picker.hour, picker.minute)

                viewModel.updateNotificationTime(
                    String.format(Locale.getDefault(), "%02d:%02d", picker.hour, picker.minute)
                )
            }

            picker.show(parentFragmentManager, "timePicker")
        }
        binding.textViewAtSpecificDays.setOnClickListener {
            val picker = AppDialogs.createTimePicker()

            picker.addOnPositiveButtonClickListener {
                binding.textViewAtSpecificDays.text =
                    String.format(Locale.getDefault(), "at %02d:%02d", picker.hour, picker.minute)

                viewModel.updateNotificationTime(
                    String.format(Locale.getDefault(), "%02d:%02d", picker.hour, picker.minute)
                )
            }

            picker.show(parentFragmentManager, "timePicker")
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.navigateBack()
        }
    }
}