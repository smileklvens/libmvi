package com.ikang.staffapp.ui.fragment.me.personalcenter

import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil.setContentView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DividerItemDecoration
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ikang.libmvi.base.NoViewModel
import com.ikang.libmvi.base.ui.activity.BaseActivity
import com.ikang.libmvi.base.ui.activity.BaseRefreshMoreActivity
import com.ikang.libmvi.util.ext.observe
import com.ikang.mymodule.R
import kotlinx.android.synthetic.main.activity_personal_center.*
import kotlinx.android.synthetic.main.fragment_me.*

class PersonalCenterActivity : BaseRefreshMoreActivity<PersonalCenterModel, ViewDataBinding>() {

    private val mAdapter by lazy { PersonalListAdapter() }
    private var page: Int = 0

    override fun layoutId(): Int = R.layout.activity_personal_center

    override fun initView(savedInstanceState: Bundle?) {
        bindSwipeRecycler(
                mePersonalSmartRefreshLayout,
                meRecyclerView,
                mAdapter as BaseQuickAdapter<*, BaseViewHolder>
        )
        meRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val inflate = LayoutInflater.from(this).inflate(R.layout.layout_personal_header, null, false)
        mAdapter.addHeaderView(inflate)

        observe(viewModel.meListBean) {
            stopRefresh()
            if (it.curPage == 1) mAdapter.setNewData(it.datas)
            else mAdapter.addData(it.datas)
            page = it.curPage
        }

        mePersonalSmartRefreshLayout.setEnableLoadMore(false)
        mePersonalSmartRefreshLayout.setEnableRefresh(false)
    }

    fun lazyLoadData() {
        viewModel.getMeListBean(page)
    }

    override fun initData() {

    }

    override fun onItemLongClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int): Boolean {
        return false
    }

    override fun onItemClick(
            adapter: BaseQuickAdapter<*, BaseViewHolder>,
            view: View,
            position: Int
    ) {
        intent.component = ComponentName("com.ikang.staffapp.ui.login","com.ikang.staffapp.ui.login.LoginActivity")
        startActivity(intent)
    }


    override fun onRefresh() {
        //刷新
        page = 0
        viewModel.getMeListBean(page)
    }

    override fun onLoadMore() {
        //加载
        viewModel.getMeListBean(page + 1)
    }
}
