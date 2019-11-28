package com.ikang.staffapp.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.*


/**
 * @author ikang-zhulk
 * @version 1.0.0
 * @describe 懒加载androidx 方案
 */
class HomePageAdapter(fm: FragmentManager, behavior: Int, val stack: Stack<Fragment>) :
    FragmentPagerAdapter(fm, behavior) {

    override fun getItem(position: Int): Fragment {
        return stack.get(position)
    }

    /**
     * Return the number of views available.
     */
    override fun getCount(): Int = stack.size
}