package com.example.bachelorwork.ui.common.base

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.example.bachelorwork.ui.utils.dialogs.createDiscardDialog
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


abstract class BaseBottomSheetDialogFragment<VB : ViewBinding> : BottomSheetDialogFragment() {

    protected abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    private var _binding: VB? = null

    open val TAG: String = this::class.java.simpleName

    protected val binding
        get() = requireNotNull(_binding)

    private val customToolbar: MaterialToolbar?
        get() = setupCustomToolbar()

    private val customAppBarLayout: AppBarLayout?
        get() = setupAppBarLayout()

    protected open val onBackPressedDispatcher: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                createDiscardDialog { dismiss() }.show()
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
            this@BaseBottomSheetDialogFragment.onBackPressedDispatcher.let {
                onBackPressedDispatcher.addCallback(this, it)
            }
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (!isFragmentAlreadyCreated(manager, tag ?: TAG)) {
            super.show(manager, tag ?: TAG)
        }
    }

    override fun showNow(manager: FragmentManager, tag: String?) {
        if (!isFragmentAlreadyCreated(manager, tag ?: TAG)) {
            super.showNow(manager, tag ?: TAG)
        }
    }

    private fun isFragmentAlreadyCreated(manager: FragmentManager, tag: String): Boolean {
        val existingFragment = manager.findFragmentByTag(tag)
        return existingFragment != null && existingFragment.isAdded && !existingFragment.isDetached
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
