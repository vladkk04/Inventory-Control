package com.example.inventorycotrol.ui.common.base

import android.app.Dialog
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.annotation.MenuRes
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.viewbinding.ViewBinding
import com.example.inventorycotrol.R
import com.example.inventorycotrol.ui.common.AppDialogs
import com.example.inventorycotrol.ui.snackbar.SnackbarController
import com.example.inventorycotrol.ui.utils.screen.InsetHandler
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_DRAGGING
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.bottomsheet.BottomSheetDragHandleView
import com.google.android.material.progressindicator.LinearProgressIndicator


abstract class BaseBottomSheetDialogFragment<VB : ViewBinding> : BottomSheetDialogFragment() {
    private var _binding: VB? = null

    private var toolbar: MaterialToolbar? = null

    private var dragHandleView: BottomSheetDragHandleView? = null

    protected abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    protected val binding
        get() = requireNotNull(_binding)

    protected open val titleToolbar: String = "Bottom Sheet"

    protected open val navigationIcon: Int = R.drawable.ic_close

    private var progressBar: LinearProgressIndicator? = null

    @MenuRes
    protected open val inflateMenu: Int? = null

    protected open val isAddDragHandleView = true

    protected open val isFullScreen = true

    protected open val isAllowFullScreen = true

    protected open val isCancel: Boolean = true

    protected open val isCancellableOutside: Boolean = true

    override fun getTheme(): Int = R.style.ThemeOverlay_App_BottomSheetDialog

    protected open val onBackPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                AppDialogs.createDiscardDialog(requireContext()) { dismiss() }.show()
            }
        }

    protected open fun onMenuItemToolbarClickListener(menuItem: MenuItem): Boolean = false

    protected open fun onNavigationIconToolbarClickListener() = Unit

    protected open fun setupViews(): Unit = Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)

        setupViews()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SnackbarController.observeSnackbarEvents(this, binding.root.rootView)

        setupToolbar()
        setupBottomSheetDragHandleView()
        setupFullScreen()


    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            setCanceledOnTouchOutside(this@BaseBottomSheetDialogFragment.isCancellableOutside)
            setCancelable(this@BaseBottomSheetDialogFragment.isCancel)
            behavior.addBottomSheetCallback(callback)
            behavior.state = STATE_EXPANDED
            behavior.isShouldRemoveExpandedCorners = isAllowFullScreen
            onBackPressedDispatcher.addCallback(
                this@BaseBottomSheetDialogFragment,
                onBackPressedCallback
            )
        }
    }

    private val callback = object : BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == STATE_EXPANDED) {
                toolbar?.visibility = View.VISIBLE
                progressBar?.visibility = View.INVISIBLE
            }
            if (newState == STATE_DRAGGING) {
                toolbar?.visibility = View.INVISIBLE
                progressBar?.visibility = View.INVISIBLE
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            setupBottomSheetDragHandleViewAnimation(slideOffset)
        }
    }

    private fun createBottomSheetDragHandleView(): BottomSheetDragHandleView {
        return BottomSheetDragHandleView(requireContext()).apply {
            measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            layout(0, 0, measuredWidth, measuredHeight / 2)

            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            }
        }
    }

    private fun createToolbar(): ViewGroup {
        val typedValue = TypedValue()

        requireContext().theme.resolveAttribute(
            com.google.android.material.R.attr.colorSurfaceContainerLow,
            typedValue,
            true
        )

        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        toolbar = MaterialToolbar(requireContext()).apply {
            title = titleToolbar
            minimumHeight = resources.getDimensionPixelSize(androidx.appcompat.R.dimen.abc_action_bar_default_height_material)
            inflateMenu?.let { inflateMenu(it) }
            measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            layout(0, 0, measuredWidth, measuredHeight)
            setBackgroundColor(typedValue.data)
            inflateMenu(R.menu.base_bottom_sheet_menu)
            setNavigationIcon(this@BaseBottomSheetDialogFragment.navigationIcon)
            setNavigationOnClickListener { onNavigationIconToolbarClickListener() }
            setupToolbarMenu(this)
            InsetHandler.adaptToEdgeWithPadding(this)
        }

        layout.addView(toolbar)
        layout.addView(createLinearProgressBar())
        return layout
    }

    protected fun showProgress(value: Boolean) {
        progressBar?.visibility = if (value) View.VISIBLE else View.GONE
    }
    private fun createLinearProgressBar(): LinearProgressIndicator {
        progressBar = LinearProgressIndicator(requireContext()).apply {
            isIndeterminate = true
            isVisible = false
        }
        return progressBar!!
    }

    private fun setupBottomSheetDragHandleView() {
        if (!isAddDragHandleView) return

        dragHandleView = createBottomSheetDragHandleView().also {
            (requireView().parent as? ViewGroup)?.addView(it, 0)
            (requireView() as? ViewGroup)?.updatePadding(top = it.height)
        }
    }

    private fun setupToolbar() {
        if (!isAllowFullScreen) return

        val toolbarContainer = createToolbar()

        dialog?.addContentView(
            toolbarContainer,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
    }

    private fun setupToolbarMenu(toolbar: MaterialToolbar) {
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.adjust -> {
                    (dialog as BottomSheetDialog).behavior.state = STATE_COLLAPSED
                    toolbar.visibility = View.INVISIBLE
                    progressBar?.visibility = View.INVISIBLE
                }
            }
            onMenuItemToolbarClickListener(menuItem)
        }
        toolbar.setNavigationOnClickListener {
            onNavigationIconToolbarClickListener()
        }
    }

    private fun setupBottomSheetDragHandleViewAnimation(offset: Float) {
        dragHandleView?.animate()?.alpha(1 - offset)?.setDuration(0)
    }

    private fun setupFullScreen() {
        if(!isFullScreen) return

        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        (dialog as BottomSheetDialog).apply {
            behavior.maxHeight = resources.displayMetrics.heightPixels - (toolbar?.height ?: 0) + (dragHandleView?.height ?: 0) + (progressBar?.height ?: 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
