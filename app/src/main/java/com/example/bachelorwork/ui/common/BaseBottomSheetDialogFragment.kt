package com.example.bachelorwork.ui.common

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.viewbinding.ViewBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
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
        get() = setupCustomToolbar()

    private val customAppBarLayout: AppBarLayout?
        get() = setupAppBarLayout()

    protected open val onBackPressedDispatcher: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showDiscardDialog(
                    positiveButtonAction = { dismiss() },
                )
            }
        }

    protected open fun onMenuItemToolbarClickListener(menuItem: MenuItem): Boolean = false

    protected open fun onNavigationIconToolbarClickListener() = Unit

    protected open fun setupCustomToolbar(): MaterialToolbar? = null

    protected open fun setupAppBarLayout(): AppBarLayout? = null

    protected open fun setupViews(): Unit = Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        setupViews()
        setupToolbarOnClickListeners()
        return binding.root
    }

    private fun setupToolbarOnClickListeners() {
        customToolbar?.setNavigationOnClickListener {
            onNavigationIconToolbarClickListener()
        }
        customToolbar?.setOnMenuItemClickListener { menuItem ->
            onMenuItemToolbarClickListener(menuItem)
        }
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
                onBackPressedDispatcher.addCallback(this, it)
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
