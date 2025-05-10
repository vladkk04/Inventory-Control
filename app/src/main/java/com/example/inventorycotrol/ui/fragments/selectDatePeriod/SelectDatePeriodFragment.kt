package com.example.inventorycotrol.ui.fragments.selectDatePeriod

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.inventorycotrol.databinding.FragmentSelectDatePeriodBinding
import com.example.inventorycotrol.domain.model.TimePeriod
import com.example.inventorycotrol.ui.common.base.BaseBottomSheetDialogFragment
import com.example.inventorycotrol.ui.utils.screen.InsetHandler
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SelectDatePeriodFragment : BaseBottomSheetDialogFragment<FragmentSelectDatePeriodBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSelectDatePeriodBinding
        get() = FragmentSelectDatePeriodBinding::inflate

    private val viewModel: SelectDatePeriodViewModel by viewModels()

    override val isAllowFullScreen: Boolean = false

    override val isFullScreen: Boolean = false

    override fun setupViews() {
        super.setupViews()
        InsetHandler.adaptToEdgeWithPadding(binding.root)

        setupSelectionWithImageView()

    }


    private fun setupSelectionWithImageView() {

        var currentSelectedLayout: LinearLayout? = null

        val layoutToImageViewPairs = listOf(
            binding.layoutToday to binding.imageViewToday,
            binding.layoutThreeDay to binding.imageViewThreeDays,
            binding.layoutLastSevenDays to binding.imageViewLastSevenDays,
            binding.layoutLastThirtyDays to binding.imageViewLastThirtyDays,
            binding.layoutLastNinetyDays to binding.imageViewLastNinetyDays,
            binding.layoutLastYear to binding.imageViewLastYear
        )

        layoutToImageViewPairs.forEach { (layout, imageView) ->
            layout.setOnClickListener {
                layoutToImageViewPairs.forEach { (_, iv) -> iv.isVisible = false }

                imageView.isVisible = true

                currentSelectedLayout = layout

                when (layout.id) {
                    binding.layoutToday.id -> TimePeriod.TODAY
                    binding.layoutThreeDay.id -> TimePeriod.LAST_3_DAYS
                    binding.layoutLastSevenDays.id -> TimePeriod.LAST_7_DAYS
                    binding.layoutLastThirtyDays.id -> TimePeriod.LAST_MONTH
                    binding.layoutLastNinetyDays.id -> TimePeriod.LAST_90_DAYS
                    binding.layoutLastYear.id -> TimePeriod.LAST_YEAR
                    else -> { null }
                }?.let {
                    viewModel.selectPeriod(it)
                }
            }

            imageView.isVisible = layout == currentSelectedLayout
        }
    }


}