package com.example.bachelorwork.ui.fragments.more

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import com.example.bachelorwork.databinding.FragmentMoreBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreFragment: BottomSheetDialogFragment() {

    private var _binding: FragmentMoreBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoreBinding.inflate(inflater, container, false)
        setupFabButtons()
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
            setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            setWindowAnimations(0)
            setDimAmount(0f)

            findViewById<View>(com.google.android.material.R.id.touch_outside)?.apply {
                animateBackgroundColor()
                setOnClickListener {
                    (dialog as BottomSheetDialog).behavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }

            attributes = attributes.apply {
                gravity = Gravity.TOP
                height = getAdjustedHeightWithBottomNavigationBar()
            }
        }
    }


    private fun getFullScreenHeight(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            windowManager.currentWindowMetrics.bounds.height()
        } else {
            DisplayMetrics().also { metrics ->
                @Suppress("DEPRECATION")
                windowManager.defaultDisplay.getMetrics(metrics)
            }.heightPixels
        }
    }

    private fun getAdjustedHeightWithBottomNavigationBar(): Int {
        val statusBarHeight = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 80f, requireContext().resources.displayMetrics
        ).toInt()

        return getFullScreenHeight(requireContext()) - statusBarHeight
    }

    private fun View.animateBackgroundColor() {
        ObjectAnimator.ofObject(
            this, "backgroundColor", ArgbEvaluator(),
            Color.TRANSPARENT, Color.parseColor("#80000000")
        ).setDuration(200).start()
    }

    private fun setupFabButtons() {
        binding.fabManageUsers.setOnClickListener {
            viewModel.navigateToManageUsers()
        }
        binding.fabManageOrganisation.setOnClickListener {
            viewModel.navigateToManageOrganisation()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            setCanceledOnTouchOutside(false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
