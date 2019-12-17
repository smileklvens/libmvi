package com.ikang.staffapp.ui.fragment.me.setting

import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DividerItemDecoration
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ikang.libmvi.base.ui.activity.BaseRefreshMoreActivity
import com.ikang.libmvi.util.ext.observe
import com.ikang.mymodule.R
import com.ikang.staffapp.ui.fragment.me.MeListAdapter
import com.ikang.staffapp.ui.fragment.me.personalcenter.PersonalCenterModel
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.fragment_me.*

class SettingActivity : BaseRefreshMoreActivity<PersonalCenterModel, ViewDataBinding>() {

    private val mAdapter by lazy { MeListAdapter() }
    private var page: Int = 0

    override fun onLoadMore() {
    }

    override fun layoutId(): Int = R.layout.activity_setting

    override fun initView(savedInstanceState: Bundle?) {
        bindSwipeRecycler(
                meSettingSmartRefreshLayout,
                meRecyclerView,
                mAdapter as BaseQuickAdapter<*, BaseViewHolder>
                )
        meRecyclerView.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))

        observe(viewModel.meListBean){
            stopRefresh()
            if (it.curPage == 1) mAdapter.setNewData(it.datas)
            else mAdapter.addData(it.datas)
            page = it.curPage
        }

        meSettingSmartRefreshLayout.setEnableLoadMore(false)
        meSettingSmartRefreshLayout.setEnableRefresh(false)
    }

    fun lazyLoadData() {
        viewModel.getMeListBean(page)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, BaseViewHolder>?, view: View?, position: Int) {
        val intent = Intent()
        intent.component = ComponentName("com.ikang.staffapp.ui.login","com.ikang.staffapp.ui.login.LoginActivity")
        startActivity(intent)
    }

    override fun onItemLongClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int): Boolean {
        return false
    }
}
