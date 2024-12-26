package com.example.bachelorwork.ui.views

import android.content.Context
import android.util.AttributeSet
import android.util.Size
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.PopupWindow
import com.example.bachelorwork.databinding.CustomFloatingMenuButtonBinding
import com.example.bachelorwork.databinding.CustomFloatingMenuTriggerBinding

class CustomFloatingMenu @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val triggerBinding: CustomFloatingMenuTriggerBinding =
        CustomFloatingMenuTriggerBinding.inflate(LayoutInflater.from(context), this, true)

    private val popupBinding = CustomFloatingMenuButtonBinding.inflate(LayoutInflater.from(context))

    private var popupWindow: PopupWindow? = null

    init {
        triggerBinding.fabMain.setOnClickListener {
            if (popupWindow?.isShowing == true) {
                dismissPopup()
            } else {
                showPopup()
            }
        }
        popupBinding.fabClose.setOnClickListener {
            dismissPopup()
        }
    }

    private fun showPopup() {
        if (popupWindow == null) {
            popupWindow = PopupWindow(
                popupBinding.root,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                true
            ).apply {
                isOutsideTouchable = true
                popupBinding.root.measure(0, 0)
            }
        }

        val location = IntArray(2)
        triggerBinding.fabMain.getLocationOnScreen(location)

        val contentSize = Size(
            popupBinding.root.measuredWidth,
            popupBinding.root.measuredHeight
        )

        popupWindow?.apply {
            showAtLocation(
                triggerBinding.fabMain,
                Gravity.NO_GRAVITY,
                location[0] - (contentSize.width - triggerBinding.fabMain.width),
                location[1] - contentSize.height + triggerBinding.fabMain.height
            )
        }

        addDimBehind()
    }

    private fun dismissPopup() {
        popupWindow?.dismiss()
        popupWindow = null
    }

    fun setOnCreateItemClickListener(onClick: () -> Unit) {
        popupBinding.fabCreateItem.setOnClickListener {
            onClick()
            dismissPopup()
        }
    }

    fun setOnCreateOrderClickListener(onClick: () -> Unit) {
        popupBinding.fabCreateOrder.setOnClickListener {
            onClick()
            dismissPopup()
        }
    }

    fun setOnCreateUserClickListener(onClick: () -> Unit) {
        popupBinding.fabCreateUser.setOnClickListener {
            onClick()
            dismissPopup()
        }
    }

    private fun addDimBehind(dimAmount: Float = 0.5f) {
        popupWindow?.contentView?.rootView?.let { container ->
            val context = container.context
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val layoutParams = container.layoutParams as WindowManager.LayoutParams
            layoutParams.flags = layoutParams.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
            layoutParams.dimAmount = dimAmount
            wm.updateViewLayout(container, layoutParams)
        }
    }
}

