package com.example.bachelorwork.ui.common

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginTop
import androidx.core.view.updatePadding
import androidx.viewbinding.ViewBinding
import com.example.bachelorwork.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.bottomsheet.BottomSheetDragHandleView
import com.google.android.material.color.MaterialColors

abstract class BaseBottomSheetDialogFragment<T : ViewBinding> : BottomSheetDialogFragment() {

    private var _binding: T? = null

    private val binding
        get() = requireNotNull(_binding)

    open val TAG: String = this::class.java.simpleName

    private var appBarLayout: AppBarLayout? = null
    private var dragHandleView: BottomSheetDragHandleView? = null

    companion object {
        private const val EXPANDED_STATE_THRESHOLD = 0.93f
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = setupViewBinding(inflater, container)

        appBarLayout = binding.root.findViewById(R.id.bottom_sheet_app_bar_layout)
        dragHandleView = binding.root.findViewById(R.id.bottom_sheet_drag_handle_view)

        return binding.root
    }

    abstract fun setupViewBinding(inflater: LayoutInflater, container: ViewGroup?): T

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog

            bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)?.let { bottomSheet ->
                val behavior = BottomSheetBehavior.from(bottomSheet)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                bottomSheet.fitsSystemWindows = false
                setupFullHeight(bottomSheet)

                behavior.addBottomSheetCallback(object : BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        Log.d("debug", bottomSheet.paddingTop.toString())
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                        TODO("Not yet implemented")
                    }

                })

            }
        }
        return dialog
    }


    private fun setupFullHeight(bottomSheetLayout: FrameLayout) {
        val layoutParams = bottomSheetLayout.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheetLayout.layoutParams = layoutParams
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}