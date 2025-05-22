package com.example.inventorycotrol.ui.fragments.organisationSettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.inventorycotrol.R
import com.example.inventorycotrol.data.remote.dto.NotificationSettings
import com.example.inventorycotrol.data.remote.dto.ThresholdSettings
import com.example.inventorycotrol.databinding.FragmentOrganisationSettingsBinding
import com.example.inventorycotrol.domain.model.organisation.OrganisationRole
import com.example.inventorycotrol.ui.MainViewModel
import com.example.inventorycotrol.ui.common.AppDialogs
import com.example.inventorycotrol.ui.views.createOrganisationRoleChip
import com.example.inventorycotrol.ui.worker.StockCheckWorker
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class OrganisationSettingsFragment : Fragment() {

    private var _binding:FragmentOrganisationSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OrganisationSettingsViewModel by viewModels()

    private val mainViewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrganisationSettingsBinding.inflate(inflater, container, false)
        setupToolbar()
        setupTimePicker()
        setupChipGroup()
        setupToggleButton()
        setupNotifyBy()

        setupEditText()


        binding.buttonSave.setOnClickListener {
            viewModel.saveSettings {
                if (it) {
                    scheduleStockCheckWorker(viewModel.notificationSettings.value, viewModel.thresholdSettings.value)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main.immediate) {
            mainViewModel.isConnected.collectLatest {
                binding.buttonSave.isEnabled = it
                binding.toggleButton.children.forEach { button -> button.isEnabled = it }
                binding.chipGroupOrganisationRole.children.forEach { chip -> chip.isEnabled = it }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                updateUiState(state)
            }
        }

        return binding.root
    }
    private fun scheduleStockCheckWorker(
        settings: NotificationSettings,
        thresholdSettings: ThresholdSettings
    ) {
        val workManager = WorkManager.getInstance(requireContext())

        if (!settings.notifiableRoles.contains(mainViewModel.organisationRole.value)) {
            workManager.cancelUniqueWork("stockCheck")
            return
        }

        workManager.cancelUniqueWork("stockCheck")

        val notificationTimeParts = settings.notificationTime.split(":")
        val hour = notificationTimeParts[0].toInt()
        val minute = notificationTimeParts.getOrElse(1) { "0" }.toInt()

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        val initialDelay = calendar.timeInMillis - System.currentTimeMillis()

        val inputData = workDataOf(
            "critical_threshold_percentage" to thresholdSettings.criticalThresholdPercentage,
            "notification_days" to settings.notificationDays.toIntArray()
        )

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequestBuilder<StockCheckWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "stockCheck",
            ExistingPeriodicWorkPolicy.UPDATE, request
        )
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
        binding.toggleButton.addOnButtonCheckedListener { group, _, _ ->
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
        binding.radioButtonEveryDay.setOnCheckedChangeListener { _, b ->
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

        binding.radioButtonSpecificDays.setOnCheckedChangeListener { _, b ->
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
            val time = binding.textViewAtEveryDay.text.toString().removePrefix("at ").split(":").map { it.toInt() }


            val picker = AppDialogs.createTimePicker(
                time.component1() to time.component2()
            )

            picker.addOnPositiveButtonClickListener {
                binding.textViewAtEveryDay.text =
                    String.format(Locale.getDefault(), "at %02d:%02d", picker.hour, picker.minute)

                viewModel.updateNotificationTime(
                    String.format(Locale.getDefault(), "%02d:%02d", picker.hour, picker.minute)
                )
            }

            picker.show(parentFragmentManager, "timePicker1")
        }
        binding.textViewAtSpecificDays.setOnClickListener {
            val time = binding.textViewAtSpecificDays.text.toString().removePrefix("at ").split(":").map { it.toInt() }

            val picker = AppDialogs.createTimePicker(
                time.component1() to time.component2()
            )


            picker.addOnPositiveButtonClickListener {
                binding.textViewAtSpecificDays.text =
                    String.format(Locale.getDefault(), "at %02d:%02d", picker.hour, picker.minute)

                viewModel.updateNotificationTime(
                    String.format(Locale.getDefault(), "%02d:%02d", picker.hour, picker.minute)
                )
            }

            picker.show(parentFragmentManager, "timePicker2")
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.navigateBack()
        }
    }
}