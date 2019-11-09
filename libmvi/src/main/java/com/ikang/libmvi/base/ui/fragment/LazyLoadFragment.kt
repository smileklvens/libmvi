package com.ikang.libmvi.base.ui.fragment

import android.os.Bundle
import androidx.databinding.ViewDataBinding


/**
 * @author IK-zhulk
 * @version 1.0.0
 * @describe  懒加载 1、ViewPager 嵌套 Fragment 2、FragmentManager 管理 Fragment
 */
abstract class LazyLoadFragment<VB : ViewDataBinding>: BaseFragment<VB>() {

    private var isViewCreated = false // 界面是否已创建完成
    private var isVisibleToUser = false // 是否对用户可见
    private var isDataLoaded = false // 数据是否已请求, isNeedReload()返回false的时起作用
    private var isCurrentHidden = true // 记录当前fragment的是否隐藏

    // 实现具体的数据请求逻辑
    protected abstract fun loadData()

    /**
     * 使用ViewPager嵌套fragment时，切换ViewPager回调该方法
     *
     * @param isVisibleToUser
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        tryLoadData()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isViewCreated = true
        tryLoadData()
    }


    /**
     * 使用show()、hide()控制fragment显示、隐藏时回调该方法
     *
     * @param hidden
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        isCurrentHidden = hidden
        if (!hidden) {
            tryLoadData1()
        }
    }

    /**
     * ViewPager场景下，判断父fragment是否可见
     *
     * @return
     */
    private fun isParentVisible(): Boolean {
        val fragment = parentFragment
        return fragment == null || fragment is LazyLoadFragment<*> && fragment.isVisibleToUser
    }

    /**
     * ViewPager场景下，当前fragment可见，如果其子fragment也可见，则尝试让子fragment加载请求
     */
    private fun dispatchParentVisibleState() {
        val fragmentManager = childFragmentManager
        val fragments = fragmentManager.fragments
        if (fragments.isEmpty()) {
            return
        }
        for (child in fragments) {
            if (child is LazyLoadFragment<*> && child.isVisibleToUser) {
                child.tryLoadData()
            }
        }
    }

    /**
     * fragment再次可见时，是否重新请求数据，默认为flase则只请求一次数据
     *
     * @return
     */
    protected fun isNeedReload(): Boolean {
        return false
    }

    /**
     * ViewPager场景下，尝试请求数据
     */
    fun tryLoadData() {
        if (isViewCreated && isVisibleToUser && isParentVisible() && (isNeedReload() || !isDataLoaded)) {
            loadData()
            isDataLoaded = true
            dispatchParentVisibleState()
        }
    }

    /**
     * show()、hide()场景下，当前fragment没隐藏，如果其子fragment也没隐藏，则尝试让子fragment请求数据
     */
    private fun dispatchParentHiddenState() {
        val fragmentManager = childFragmentManager
        val fragments = fragmentManager.fragments
        if (fragments.isEmpty()) {
            return
        }
        for (child in fragments) {
            if (child is LazyLoadFragment<*> && !child.isCurrentHidden) {
                child.tryLoadData1()
            }
        }
    }

    /**
     * show()、hide()场景下，父fragment是否隐藏
     *
     * @return
     */
    private fun isParentHidden(): Boolean {
        val fragment = parentFragment
        if (fragment == null) {
            return false
        } else if (fragment is LazyLoadFragment<*> && !fragment.isCurrentHidden) {
            return false
        }
        return true
    }

    /**
     * show()、hide()场景下，尝试请求数据
     */
    fun tryLoadData1() {
        if (!isParentHidden() && (isNeedReload() || !isDataLoaded)) {
            loadData()
            isDataLoaded = true
            dispatchParentHiddenState()
        }
    }

    override fun onDestroy() {
        isViewCreated = false
        isVisibleToUser = false
        isDataLoaded = false
        isCurrentHidden = true
        super.onDestroy()
    }


}