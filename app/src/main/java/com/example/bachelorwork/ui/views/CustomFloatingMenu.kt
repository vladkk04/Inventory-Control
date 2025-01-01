package com.example.bachelorwork.ui.views

import android.content.Context
import android.util.AttributeSet
import android.util.Printer
import android.util.Size
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.PopupWindow
import androidx.appcompat.widget.PopupMenu
import com.example.bachelorwork.databinding.CustomFloatingActionButtonMenuBinding

class CustomFloatingMenu (
    private val context: Context,
    private val anchorView: View
) {
    private val popupBinding =
        CustomFloatingActionButtonMenuBinding.inflate(LayoutInflater.from(context))

    private var popupWindow : PopupWindow? = null


    init {
        anchorView.setOnClickListener {
            if (popupWindow?.isShowing == true)
            {
                dismissPopup()
            } else
            {
                showPopup()
            }
        }
        popupBinding.fabClose.setOnClickListener {
            dismissPopup()
        }
    }


    fun setOnCreateItemClickListener(onClick : () -> Unit)
    {
        popupBinding.fabCreateItem.setOnClickListener {
            onClick()
            dismissPopup()
        }
    }

    fun setOnCreateOrderClickListener(onClick : () -> Unit)
    {
        popupBinding.fabCreateOrder.setOnClickListener {
            onClick()
            dismissPopup()
        }
    }

    fun setOnCreateUserClickListener(onClick : () -> Unit)
    {
        popupBinding.fabCreateUser.setOnClickListener {
            onClick()
            dismissPopup()
        }
    }

    private fun dismissPopup() {
        popupWindow?.dismiss()
        popupWindow = null
    }

    private fun showPopup() {
        if (popupWindow == null)
        {
            popupWindow = PopupWindow(
                popupBinding.root,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
            ).apply {
                isOutsideTouchable = true
                popupBinding.root.measure(0,0)
            }
        }

        val location = IntArray(2)
        anchorView.getLocationOnScreen(location)

        val contentSize = Size(
            popupBinding.root.measuredWidth,
            popupBinding.root.measuredHeight
        )

        popupWindow?.apply {
            showAtLocation(
                anchorView,
                Gravity.NO_GRAVITY,
                location[0] - (contentSize.width - anchorView.width),
                location[1] - contentSize.height + anchorView.height
            )
        }

        addDimBehind()
    }

    private fun addDimBehind(dimAmount : Float = 0.5f)
    {
        popupWindow?.contentView?.rootView?.let { container ->
            val context = container.context
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val layoutParams = container.layoutParams as WindowManager.LayoutParams
            layoutParams.flags = layoutParams.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
            layoutParams.dimAmount = dimAmount
            wm.updateViewLayout(container,layoutParams)
        }
    }
}

