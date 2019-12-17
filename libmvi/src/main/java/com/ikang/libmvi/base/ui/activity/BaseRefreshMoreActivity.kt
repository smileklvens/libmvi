package com.ikang.libmvi.base.ui.activity

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ikang.libmvi.base.BaseViewModel
import com.ikang.libmvi.base.ui.fragment.BaseRefreshFragment
import com.scwang.smartrefresh.layout.SmartRefreshLayout

/**
 * @author ikang-renwei
 * @Date 2019/12/05 15:55
 * @describe
 */
abstract class  BaseRefreshMoreActivity<VM :BaseViewModel,DB :ViewDataBinding> :
        BaseActivity<VM, DB>(),
        BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemLongClickListener {

    lateinit var mRefresh: SmartRefreshLayout

    fun bindRefreshLayout(refreshLayout: SmartRefreshLayout) {
        mRefresh = refreshLayout
        mRefresh.setOnRefreshListener { onRefresh() }
    }

    /** 下拉刷新回调 */
    abstract fun onLoadMore()

    fun bindSwipeRecycler(
            refreshLayout: SmartRefreshLayout,
            recyclerView: RecyclerView,
            adapter: BaseQuickAdapter<*, BaseViewHolder>
    ) {
        bindSwipeRecycler(refreshLayout, recyclerView, LinearLayoutManager(this), adapter)
    }


    fun bindSwipeRecycler(
            refreshLayout: SmartRefreshLayout,
            recyclerView: RecyclerView,
            lm: RecyclerView.LayoutManager,
            mAdapter: BaseQuickAdapter<*, BaseViewHolder>
    ) {
        bindRefreshLayout(refreshLayout)

        with(recyclerView) {
            layoutManager = lm
            adapter = mAdapter
        }
        mAdapter.onItemClickListener = this
        mAdapter.onItemLongClickListener = this
        mRefresh.setOnLoadMoreListener {
            onLoadMore()
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun initData() {
    }

    /** 关闭刷新  */
    fun stopRefresh() {
        this.mRefresh.finishRefresh();
    }
}