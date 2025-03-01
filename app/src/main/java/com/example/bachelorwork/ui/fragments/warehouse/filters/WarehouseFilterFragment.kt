package com.example.bachelorwork.ui.fragments.warehouse.filters

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentWarehouseFiltersBinding
import com.example.bachelorwork.ui.model.filters.WarehouseFilterUiState
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import com.example.bachelorwork.ui.utils.extensions.viewBinding
import com.example.bachelorwork.ui.utils.screen.InsetHandler
import com.google.android.material.chip.Chip
import com.google.android.material.slider.RangeSlider
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class WarehouseFilterFragment : Fragment(R.layout.fragment_warehouse_filters) {

    private val binding by viewBinding(FragmentWarehouseFiltersBinding::bind)

    private val viewModel: WarehouseFilterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        InsetHandler.adaptToEdgeWithMargin(binding.root)

        collectInLifecycle(
            viewModel.uiState,
            lifecycleState = Lifecycle.State.STARTED
        ) { uiState ->
            updateUiState(uiState)
        }

        setupToolbar()
        setupPriceRangeSlider()
        setupPriceRangeLayout()
        setupToggleButtons()
    }

    private fun updateUiState(uiState: WarehouseFilterUiState) {
        uiState.categories.forEach { category ->
            val categoryChip = layoutInflater.inflate(
                R.layout.chip_select_category,
                binding.chipGroupCategories,
                false
            ) as Chip

            categoryChip.text = category.name

            binding.chipGroupCategories.addView(categoryChip)
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.apply -> {
                    true
                }

                else -> false
            }
        }
    }

    private fun setupToggleButtons() {
        with(binding) {
            handleToggleButton(checkBoxToggleStock, layoutStock, textViewStockSelection) {
                getSelectionText(listOf(checkBoxOverstock, checkBoxLowStock, checkBoxOutOfStock))
            }
            handleToggleButton(
                checkBoxToggleCategory,
                binding.chipGroupCategories,
                textViewCategorySelection
            ) {
                getSelectionText(binding.chipGroupCategories.children.filterIsInstance<Chip>().toList())
            }
            handleToggleButton(checkBoxToggleTags, binding.customInputTags, textViewTagsSelection) {
                binding.customInputTags.tags.joinToString(", ") { it.name }
            }
            handleToggleButton(
                checkBoxTogglePrice,
                binding.layoutContentPrice,
                textViewPriceSelection
            ) {
                ""
            }
        }
    }

    private fun setupPriceRangeLayout() {
        binding.radioButtonPriceRange.setOnCheckedChangeListener { _, b ->
            val visibility = if (b) View.VISIBLE else View.GONE

            binding.layoutInputPriceRange.visibility = visibility
            binding.rangeSliderPrice.visibility = visibility
        }
    }

    private fun setupPriceRangeSlider() {
        binding.rangeSliderPrice.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {}

            override fun onStopTrackingTouch(slider: RangeSlider) {
                binding.editTextFrom.setText(String.format(Locale.getDefault(), "%.2f", slider.values[0]))
                binding.editTextTo.setText(String.format(Locale.getDefault(), "%.2f", slider.values[1]))
            }
        })
    }

    private fun getSelectionText(checkBoxes: List<CompoundButton>): String = 
        checkBoxes.filter { it.isChecked }.joinToString(", ") { it.text }

    private fun handleToggleButton(
        button: CompoundButton,
        content: View,
        selectionContent: TextView,
        selectionText: () -> String = { "" },
    ) {
        button.setOnCheckedChangeListener { _, isChecked ->
            val contentVisibility = if (isChecked) View.VISIBLE else View.GONE

            val selectionContentVisibility =
                if (!isChecked && selectionText.invoke().isNotEmpty()) {
                    selectionContent.text = selectionText.invoke()
                    View.VISIBLE
                } else {
                    View.GONE
                }

            content.visibility = contentVisibility
            selectionContent.visibility = selectionContentVisibility

        }
    }

    override fun onDestroy() {
        Log.d("debug", "hflsj")
        super.onDestroy()
    }

}