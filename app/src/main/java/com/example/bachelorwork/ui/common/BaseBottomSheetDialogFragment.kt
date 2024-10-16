package com.example.bachelorwork.ui.common

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.viewbinding.ViewBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


abstract class BaseBottomSheetDialogFragment<VB : ViewBinding> : BottomSheetDialogFragment() {

    open val TAG: String = this::class.java.simpleName

    protected abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    private var _binding: VB? = null

    protected val binding
        get() = requireNotNull(_binding)

    private val customToolbar: MaterialToolbar?
        get() = setupToolbar()

    private val customAppBarLayout: AppBarLayout?
        get() = setupAppBarLayout()

    protected open val onBackPressedDispatcher: OnBackPressedCallback? = null

    protected open fun setupToolbar(): MaterialToolbar? = null

    protected open fun setupAppBarLayout(): AppBarLayout? = null

    protected open fun setupViews() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        setupViews()
        customToolbar?.let {
            (dialog as BottomSheetDialog).behavior.addBottomSheetCallback(
                bottomSheetCallback
            )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFullScreen()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            behavior.state = STATE_EXPANDED
            behavior.isShouldRemoveExpandedCorners = true
            this@BaseBottomSheetDialogFragment.onBackPressedDispatcher?.let {
                onBackPressedDispatcher.addCallback(
                    this,
                    it
                )
            }
        }
    }

    private val bottomSheetCallback = object : BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            customToolbar?.visibility = if (newState == STATE_EXPANDED) View.VISIBLE else View.GONE
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            //customToolbar?.animate()?.y(slideOffset * 100)?.setDuration(0)?.start()
        }
    }

    private fun setupFullScreen() {
        /*Bottom Sheet Container*/
        (requireView().parent as? ViewGroup)?.let {
            it.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        }

        /*dialog?.window?.apply {
            setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }*/
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
