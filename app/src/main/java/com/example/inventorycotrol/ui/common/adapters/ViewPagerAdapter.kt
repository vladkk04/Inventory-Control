package com.example.inventorycotrol.ui.common.adapters

import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

data class ViewPagerFragmentData(
    val fragment: Fragment,
    val title: String,
    @DrawableRes val iconResId: Int? = null
)

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private var fragmentList: List<ViewPagerFragmentData> = emptyList()

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position].fragment

    fun addFragments(vararg fragment: ViewPagerFragmentData) {
        fragmentList = fragment.toList()
    }

    fun getTitle(position: Int): String = fragmentList[position].title

    @DrawableRes
    fun getIcon(position: Int): Int? = fragmentList[position].iconResId

}