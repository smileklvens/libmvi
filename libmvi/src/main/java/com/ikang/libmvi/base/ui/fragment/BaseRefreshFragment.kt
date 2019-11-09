package com.ikang.libmvi.base.ui.fragment


import androidx.databinding.ViewDataBinding
import com.scwang.smartrefresh.layout.SmartRefreshLayout


/**
 * @author IK-zhulk
 * @version 1.0.0
 * @describe 下拉刷新，上拉加载更多
 */
abstract class BaseRefreshFragment<VB : ViewDataBinding>: LazyLoadFragment<VB>() {
    lateinit var mRefresh: SmartRefreshLayout


    fun bindRefreshLayout(refreshLayout: SmartRefreshLayout) {
        mRefresh = refreshLayout
        mRefresh.setOnRefreshListener { onRefresh() }
    }


    /** 下拉刷新回调 */
    abstract fun onRefresh()


    /** 关闭刷新  */
    fun stopRefresh() {
        this.mRefresh.finishRefresh();
    }

}