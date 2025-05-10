package com.example.inventorycotrol.ui.fragments.orders.manage.edit

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.inventorycotrol.R
import com.example.inventorycotrol.databinding.FragmentOrderManageBinding
import com.example.inventorycotrol.ui.fragments.orders.manage.BaseOrderManageFragment
import com.example.inventorycotrol.ui.fragments.orders.manage.OrderManageProductSharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderEditFragment: BaseOrderManageFragment() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOrderManageBinding
        get() = FragmentOrderManageBinding::inflate

    override val titleToolbar: String = "Edit Order"

    override val viewModel: OrderEditViewModel by viewModels()

    override val sharedViewModel: OrderManageProductSharedViewModel by viewModels()


    override fun onMenuItemToolbarClickListener(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.save -> {
                //viewModel.updateOrder()
                true
            }

            else -> false
        }
    }
}