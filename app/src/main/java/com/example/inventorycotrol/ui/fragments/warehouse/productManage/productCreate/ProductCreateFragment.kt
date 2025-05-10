package com.example.inventorycotrol.ui.fragments.warehouse.productManage.productCreate

import android.view.MenuItem
import androidx.fragment.app.viewModels
import com.example.inventorycotrol.R
import com.example.inventorycotrol.ui.fragments.warehouse.productManage.BaseProductManageFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductCreateFragment: BaseProductManageFragment() {

    override val viewModel: ProductCreateViewModel by viewModels()

    override val titleToolbar: String = "Create product"

    override fun onMenuItemToolbarClickListener(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.save -> {
                viewModel.createProduct()
                true
            }

            else -> false
        }
    }
}


