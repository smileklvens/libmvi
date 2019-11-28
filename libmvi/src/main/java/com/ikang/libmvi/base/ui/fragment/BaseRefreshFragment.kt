package com.ikang.libmvi.base.ui.fragment


import androidx.databinding.ViewDataBinding
import com.ikang.libmvi.base.BaseViewModel
import com.scwang.smartrefresh.layout.SmartRefreshLayout


/**
 * @author IK-zhulk
 * @version 1.0.0
 * @describe 下拉刷新
 */
abstract class BaseRefreshFragment<VM : BaseViewModel, DB : ViewDataBinding>: BaseFragment<VM,DB>() {
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