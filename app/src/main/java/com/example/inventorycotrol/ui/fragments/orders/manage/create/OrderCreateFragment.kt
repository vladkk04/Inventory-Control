package com.example.inventorycotrol.ui.fragments.orders.manage.create

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.inventorycotrol.R
import com.example.inventorycotrol.databinding.FragmentOrderManageBinding
import com.example.inventorycotrol.ui.fragments.orders.manage.BaseOrderManageFragment
import com.example.inventorycotrol.ui.fragments.orders.manage.OrderManageProductSharedViewModel
import com.example.inventorycotrol.ui.navigation.Destination
import com.example.inventorycotrol.ui.utils.extensions.hiltViewModelNavigation
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrderCreateFragment : BaseOrderManageFragment() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOrderManageBinding
        get() = FragmentOrderManageBinding::inflate

    override val titleToolbar: String = "Create Order"

    override val viewModel: OrderCreateViewModel by viewModels()

    override val sharedViewModel: OrderManageProductSharedViewModel by hiltViewModelNavigation(Destination.CreateOrder)

    override fun onMenuItemToolbarClickListener(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.save -> {
                viewModel.createOrder()
                true
            }

            else -> false
        }
    }
}