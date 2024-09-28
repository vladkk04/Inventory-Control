package com.example.bachelorwork.ui.productCreate

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.bachelorwork.databinding.ModalBottomSheetProductCreateLayoutBinding
import com.example.bachelorwork.ui.common.BaseBottomSheetDialogFragment

class ProductCreateModalBottomSheet :
    BaseBottomSheetDialogFragment<ModalBottomSheetProductCreateLayoutBinding>() {

    override fun setupViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ModalBottomSheetProductCreateLayoutBinding {
        return ModalBottomSheetProductCreateLayoutBinding.inflate(inflater, container, false)
    }

}

