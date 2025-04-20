package com.example.bachelorwork.ui.views

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
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


    fun setOnCreateProductClickListener(onClick : () -> Unit)
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

    fun setOnInviteUserClickListener(onClick : () -> Unit)
    {
        popupBinding.fabInviteUser.setOnClickListener {
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
        val fabX = location[0]
        val fabY = location[1]

        // Get view dimensions
        val fabWidth = anchorView.width
        val fabHeight = anchorView.height
        val popupWidth = popupBinding.root.measuredWidth
        val popupHeight = popupBinding.root.measuredHeight

        val x = fabX + fabWidth - popupWidth
        val y = fabY - popupHeight + fabHeight

        popupWindow?.showAtLocation(
            anchorView,  // Fallback to anchorView if rootView not available
            Gravity.NO_GRAVITY,
            x,
            y
        )

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

