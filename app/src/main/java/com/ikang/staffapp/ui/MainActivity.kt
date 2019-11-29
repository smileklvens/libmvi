package com.ikang.staffapp.ui

import android.os.Bundle
import android.view.KeyEvent
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ikang.libmvi.base.NoViewModel
import com.ikang.libmvi.base.ui.activity.BaseActivity
import com.ikang.staffapp.R
import com.kotlin.mall.ui.fragment.HomeFragment
import com.kotlin.mall.ui.fragment.MeFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


/**
 * @author ikang-zhulk
 * @version 1.0.0
 * @describe {@link #}
 */
class MainActivity : BaseActivity<NoViewModel, ViewDataBinding>() {

    //Fragment 栈管理
    private val mStack = Stack<Fragment>()
    //主界面Fragment
    private val mHomeFragment by lazy { HomeFragment.newInstance() }
    //"我的"Fragment
    private val mMeFragment by lazy { MeFragment() }

    override fun layoutId(): Int = R.layout.activity_main


    override fun initView(savedInstanceState: Bundle?) {
        initFragment()
        initPage()
        initBottomNav()
    }

    /*
     初始化Fragment栈管理
  */
    private fun initFragment() {
        mStack.add(mHomeFragment)
        mStack.add(mMeFragment)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun initPage() {
        val homePageAdapter = HomePageAdapter(
            supportFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            mStack
        )
        mViewPage.adapter = homePageAdapter
        mViewPage.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                mBottomNavBar.selectTab(position)
            }

        })
        mViewPage.currentItem = 0

    }


    /*
       初始化底部导航切换事件
    */
    private fun initBottomNav() {
        mBottomNavBar.setTabSelectedListener(object :
            BottomNavigationBar.SimpleOnTabSelectedListener() {
            override fun onTabSelected(position: Int) {
                mViewPage.currentItem = position
            }
        })
    }


    override fun initData() {
    }
}