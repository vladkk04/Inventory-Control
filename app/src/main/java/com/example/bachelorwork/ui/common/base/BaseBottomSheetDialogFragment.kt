package com.example.bachelorwork.ui.common.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.example.bachelorwork.ui.utils.dialogs.createDiscardDialog
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.bottomsheet.BottomSheetDragHandleView


abstract class BaseBottomSheetDialogFragment<VB : ViewBinding> : BottomSheetDialogFragment() {

    protected abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    private var _binding: VB? = null

    protected val binding
        get() = requireNotNull(_binding)

    private val customToolbar: MaterialToolbar?
        get() = setupCustomToolbar()

    private val bottomSheetDragHandleView: BottomSheetDragHandleView?
        get() = setupCustomSheetDragHandleView()

    protected open val onBackPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                createDiscardDialog { dismiss() }.show()
            }
        }

    protected open val isAddDragHandleView = true

    protected open fun onMenuItemToolbarClickListener(menuItem: MenuItem): Boolean = false

    protected open fun onNavigationIconToolbarClickListener() = Unit

    protected open fun setupCustomSheetDragHandleView(): BottomSheetDragHandleView? = null

    protected open fun setupCustomToolbar(): MaterialToolbar? = null

    protected open fun setupViews(): Unit = Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        setupViews()
        setupToolbarOnClickListeners()
        //setupBottomSheetDragHandleView()
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

    private fun setupBottomSheetDragHandleView() {
        if (!isAddDragHandleView) return

        bottomSheetDragHandleView?.let {
            return addBottomSheetDragHandler(it)
        }


        if (bottomSheetDragHandleView == null) {
            addBottomSheetDragHandler(createBottomSheetDragHandler())
        } else {
            addBottomSheetDragHandler(bottomSheetDragHandleView!!)
        }
    }

    override fun onStart() {
        super.onStart()
        setupFullScreen()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            behavior.state = STATE_EXPANDED
            behavior.isShouldRemoveExpandedCorners = true
            onBackPressedDispatcher.addCallback(
                this@BaseBottomSheetDialogFragment,
                onBackPressedCallback
            )
        }
    }

    private fun isFragmentAlreadyCreated(manager: FragmentManager, tag: String): Boolean {
        val existingFragment = manager.findFragmentByTag(tag)
        return existingFragment != null && existingFragment.isAdded && !existingFragment.isDetached
    }

    private fun setupFullScreen() {
        /*Bottom Sheet Container*/
        (requireView() as? ViewGroup)?.let {
            it.layoutParams = it.layoutParams.apply {
                width = ViewGroup.LayoutParams.MATCH_PARENT
                height = ViewGroup.LayoutParams.MATCH_PARENT
            }
            // Log.d("debug", it.toString())
            //it.addView(customToolbar)
        }


        /* dialog?.window?.apply {

             //setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
         }*/
    }

    private fun addBottomSheetDragHandler(dragHandleView: BottomSheetDragHandleView) {
       /* (requireView() as? ViewGroup)?.addView(dragHandleView,0)*/
    }

    private fun createBottomSheetDragHandler(): BottomSheetDragHandleView {
        return BottomSheetDragHandleView(binding.root.context).apply {
            updateLayoutParams {
                this.width = ViewGroup.LayoutParams.MATCH_PARENT
                this.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
