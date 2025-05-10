package com.example.inventorycotrol.ui.utils.recyclerview

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.inventorycotrol.R

class UpwardScrollButtonListener(
    context: Context,
    private val view: View,
    private val layoutManager: LayoutManager,
): OnScrollListener() {
    private val slideUpAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_up)
    private val slideDownAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_down)

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val firstVisibleItemPosition = when (layoutManager) {
            is GridLayoutManager -> layoutManager.findFirstVisibleItemPosition()
            is LinearLayoutManager -> layoutManager.findFirstVisibleItemPosition()
            else -> RecyclerView.NO_POSITION
        }

        val fabVisibility = view.visibility

        if ((firstVisibleItemPosition < 1 || dy > 0) && fabVisibility == View.VISIBLE) {
            hideFab()
        } else if (dy < 0 && firstVisibleItemPosition > 1 && fabVisibility == View.INVISIBLE) {
            showFab()
        }
    }


    private fun hideFab() {
        view.startAnimation(slideDownAnimation)
        view.visibility = View.INVISIBLE
    }

    private fun showFab() {
        view.startAnimation(slideUpAnimation)
        view.visibility = View.VISIBLE
    }
}