package com.example.inventorycotrol.ui.fragments.warehouse.filters

import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.inventorycotrol.R
import com.example.inventorycotrol.databinding.FragmentWarehouseFiltersBinding
import com.example.inventorycotrol.domain.model.product.ProductTag
import com.example.inventorycotrol.ui.model.filters.WarehouseFilterUiState
import com.example.inventorycotrol.ui.utils.extensions.collectInLifecycle
import com.example.inventorycotrol.ui.utils.extensions.viewBinding
import com.example.inventorycotrol.ui.utils.screen.InsetHandler
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WarehouseFilterFragment : Fragment(R.layout.fragment_warehouse_filters) {

    private val binding by viewBinding(FragmentWarehouseFiltersBinding::bind)

    private val viewModel: WarehouseFilterViewModel by viewModels()

    private val sharedFilterViewModel: SharedWarehouseFilterViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        InsetHandler.adaptToEdgeWithMargin(binding.root)

        collectInLifecycle(
            viewModel.uiState,
        ) { uiState ->
            updateUiState(uiState)
        }

        collectInLifecycle(
            sharedFilterViewModel.uiState,
        ) { filters ->
            updateUiWithCurrentFilters(filters)
        }

        setupToolbar()
        setupToggleButtons()
    }

    private fun updateUiState(uiState: WarehouseFilterUiState) {
        binding.chipGroupCategories.removeAllViews()

        uiState.categories.forEach { category ->
            val categoryChip = layoutInflater.inflate(
                R.layout.chip_select_category,
                binding.chipGroupCategories,
                false
            ) as Chip

            categoryChip.text = category.name
            categoryChip.isChecked = sharedFilterViewModel.uiState.value.categoryFilters.any { it.name == category.name }

            binding.chipGroupCategories.addView(categoryChip)
        }
    }

    private fun updateUiWithCurrentFilters(filters: SharedWarehouseFilterUiState) {
        binding.chipGroupCategories.children
            .filterIsInstance<Chip>()
            .forEach { chip ->
                chip.isChecked = filters.categoryFilters.any { it.name == chip.text.toString() }
            }

        with(binding) {
            checkBoxOverstock.isChecked = filters.stockFilters.contains(StockFilter.OVERSTOCK)
            checkBoxLowStock.isChecked = filters.stockFilters.contains(StockFilter.LOW_STOCK)
            checkBoxCriticalStock.isChecked = filters.stockFilters.contains(StockFilter.CRITICAL_STOCK)
            checkBoxOutOfStock.isChecked = filters.stockFilters.contains(StockFilter.OUT_OF_STOCK)
        }

        binding.customInputTags.addTags(*filters.tags.map { ProductTag(it.name) }.toTypedArray())

        updateToggleButtonStates(filters)
    }

    private fun updateToggleButtonStates(filters: SharedWarehouseFilterUiState) {
        with(binding) {
            checkBoxToggleStock.isChecked = filters.stockFilters.isNotEmpty()
            checkBoxToggleCategory.isChecked = filters.categoryFilters.isNotEmpty()
            checkBoxToggleTags.isChecked = filters.tags.isNotEmpty()

            layoutStock.visibility = if (checkBoxToggleStock.isChecked) View.VISIBLE else View.GONE
            chipGroupCategories.visibility = if (checkBoxToggleCategory.isChecked) View.VISIBLE else View.GONE
            customInputTags.visibility = if (checkBoxToggleTags.isChecked) View.VISIBLE else View.GONE

            textViewStockSelection.text = getSelectionText(listOf(checkBoxOverstock, checkBoxLowStock, checkBoxOutOfStock, checkBoxCriticalStock))
            textViewStockSelection.visibility = if (!checkBoxToggleStock.isChecked && textViewStockSelection.text.isNotEmpty()) View.VISIBLE else View.GONE

            textViewCategorySelection.text = getSelectionText(chipGroupCategories.children.filterIsInstance<Chip>().toList())
            textViewCategorySelection.visibility = if (!checkBoxToggleCategory.isChecked && textViewCategorySelection.text.isNotEmpty()) View.VISIBLE else View.GONE

            textViewTagsSelection.text = customInputTags.tags.joinToString(", ") { it.name }
            textViewTagsSelection.visibility = if (!checkBoxToggleTags.isChecked && textViewTagsSelection.text.isNotEmpty()) View.VISIBLE else View.GONE
        }
    }
    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.apply -> {
                    applyFilters()
                    true
                }

                else -> false
            }
        }
    }

    private fun applyFilters() {
        val selectedCategories = viewModel.uiState.value.categories.filter { category ->
            binding.chipGroupCategories.children
                .filterIsInstance<Chip>()
                .any { chip -> chip.text == category.name && chip.isChecked }
        }

        val selectedStockFilters = mutableListOf<StockFilter>().apply {
            if (binding.checkBoxOverstock.isChecked) add(StockFilter.OVERSTOCK)
            if (binding.checkBoxLowStock.isChecked) add(StockFilter.LOW_STOCK)
            if (binding.checkBoxCriticalStock.isChecked) add(StockFilter.CRITICAL_STOCK)
            if (binding.checkBoxOutOfStock.isChecked) add(StockFilter.OUT_OF_STOCK)
        }

        val tags = binding.customInputTags.tags.map { ProductTag(it.name) }

        sharedFilterViewModel.setupFilters(selectedCategories, selectedStockFilters, tags)
        viewModel.navigateBack()
    }

    private fun setupToggleButtons() {
        with(binding) {
            handleToggleButton(checkBoxToggleStock, layoutStock, textViewStockSelection) {
                getSelectionText(listOf(checkBoxOverstock, checkBoxLowStock, checkBoxOutOfStock, checkBoxCriticalStock))
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
        }
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
}