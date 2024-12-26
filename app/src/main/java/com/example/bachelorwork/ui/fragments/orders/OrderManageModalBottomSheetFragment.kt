package com.example.bachelorwork.ui.fragments.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.bachelorwork.databinding.FragmentModalBottomSheetProductManageBinding
import com.example.bachelorwork.ui.common.base.BaseBottomSheetDialogFragment

class OrderManageModalBottomSheetFragment: BaseBottomSheetDialogFragment<FragmentModalBottomSheetProductManageBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentModalBottomSheetProductManageBinding
        get() = FragmentModalBottomSheetProductManageBinding::inflate

}