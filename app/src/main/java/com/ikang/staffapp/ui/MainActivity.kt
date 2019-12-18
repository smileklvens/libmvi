package com.ikang.staffapp.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.airbnb.lottie.LottieAnimationView
import com.ikang.libmvi.base.NoViewModel
import com.ikang.libmvi.base.ui.activity.BaseActivity
import com.ikang.staffapp.R
import com.kotlin.mall.ui.fragment.HomeFragment
import com.kotlin.mall.ui.fragment.MeFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.*


/**
 * @author ikang-zhulk
 * @version 1.0.0
 * @describe {@link #}
 */
class MainActivity : BaseActivity<NoViewModel, ViewDataBinding>(), View.OnClickListener {

    private val showFragmentCount = 5
    var bottomTvMenus = arrayOfNulls<TextView>(showFragmentCount)
    var lottieAnimationViews = arrayOfNulls<LottieAnimationView>(showFragmentCount)
    private var select_index: Int = 0


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
//                mBottomNavBar.selectTab(position)
            }

        })
        mViewPage.currentItem = 0

    }


    /*
       初始化底部导航切换事件
    */
    private fun initBottomNav() {
        lottieAnimationViews[0] = bottomMenuHomeLav
        lottieAnimationViews[1] = bottomMenuNewsLAV
        lottieAnimationViews[2] = bottomMenuWorkBenchLAV
        lottieAnimationViews[3] = bottomMenuScheduleLAV
        lottieAnimationViews[4] = bottomMenuMeRlLAV


        bottomTvMenus[0] = bottomMenuHomeTv
        bottomTvMenus[1] = bottomMenuNewsTV
        bottomTvMenus[2] = bottomMenuWorkBenchTV
        bottomTvMenus[3] = bottomMenuScheduleTV
        bottomTvMenus[4] = bottomMenuMeRlTV

        bottomMenuHomeRl.setOnClickListener(this)
        bottomMenuNewsRl.setOnClickListener(this)
        bottomMenuWorkBenchRl.setOnClickListener(this)
        bottomMenuScheduleRl.setOnClickListener(this)
        bottomMenuMeRl.setOnClickListener(this)
//        mBottomNavBar.setTabSelectedListener(object :
//            BottomNavigationBar.SimpleOnTabSelectedListener() {
//            override fun onTabSelected(position: Int) {
//                mViewPage.currentItem = position
//            }
//        })
    }

    override fun initData() {
        setViewPageCurrentItem(0)
    }


    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.bottomMenuHomeRl -> {
                    setViewPageCurrentItem(0)
                }
                R.id.bottomMenuNewsRl -> {
                    setViewPageCurrentItem(1)
                }
                R.id.bottomMenuWorkBenchRl -> {
                    setViewPageCurrentItem(2)
                }
                R.id.bottomMenuScheduleRl -> {
                    setViewPageCurrentItem(3)
                }
                R.id.bottomMenuMeRl -> {
                    setViewPageCurrentItem(4)
                }
            }
        }
    }

    private fun setViewPageCurrentItem(i: Int) {
        mViewPage.currentItem = i
        updateBottomMenuLav(lottieAnimationViews[i])
        updateBottomMenuTv(bottomTvMenus[i])
    }

    fun updateBottomMenuLav(bottomView: LottieAnimationView?) {
        bottomView?.setImageDrawable(resources.getDrawable(R.color.basic_color_placeholder))
        for (lottie in lottieAnimationViews) {
            if (lottie !== bottomView) {
                if (lottie == bottomMenuWorkBenchLAV)
                    lottie?.setImageDrawable(resources.getDrawable(R.drawable.bg_workbench_icom_list))
                else
                    lottie?.setImageDrawable(resources.getDrawable(R.color.common_white))
            }
        }
        //设置动画
//        if (lottieAnimationViews[select_index]?.progress == 0f) {
//            bottomView?.playAnimation()
//        }
//        for (view in lottieAnimationViews) {
//            if (view !== bottomView) {
//                view?.cancelAnimation()
//                view?.setProgress(0f)
//            }
//        }
    }

    fun updateBottomMenuTv(bottomTv: TextView?) {
        bottomTv?.isSelected = true
        for (tv in bottomTvMenus) {
            if (tv !== bottomTv) {
                tv?.setSelected(false)
            }
        }
    }
}