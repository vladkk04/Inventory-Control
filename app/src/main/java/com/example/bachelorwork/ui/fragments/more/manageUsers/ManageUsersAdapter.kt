package com.example.bachelorwork.ui.fragments.more.manageUsers

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.ManageUserItemBinding
import com.example.bachelorwork.ui.utils.dialogs.createDeleteDialog
import com.example.bachelorwork.ui.utils.menu.createPopupMenu
import dagger.hilt.android.AndroidEntryPoint

class ManageUsersAdapter : RecyclerView.Adapter<ManageUsersAdapter.ViewHolder>() {

    private var onDeleteUser: OnDeleteUser? = null

    fun setOnDeleteUserListener(onDeleteUser: OnDeleteUser) {
        this.onDeleteUser = onDeleteUser
    }

    inner class ViewHolder(val binding: ManageUserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val context: Context = binding.root.context

        fun bind() {
            binding.checkboxManage.setOnClickListener {
                createPopupMenu(
                    binding.root.context,
                    binding.checkboxManage,
                    R.menu.popup_manage_user_menu
                ).apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        setForceShowIcon(true)
                    }

                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.delete_user -> {
                                createDeleteDialog(
                                    context,
                                    "Vlad"
                                ) { onDeleteUser?.onDelete() }.show()
                                true
                            }

                            R.id.edit_user -> {
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ManageUserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )


    override fun getItemCount(): Int = 20

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    fun interface OnDeleteUser {
        fun onDelete()
    }
}