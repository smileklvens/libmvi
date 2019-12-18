package com.kotlin.mall.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import com.aleyn.mvvm.event.Message
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ikang.libmvi.base.ui.fragment.BaseRefreshMoreFragment
import com.ikang.libmvi.util.ext.observe
import com.ikang.providerservice.router.RouterActPath
import com.ikang.staffapp.R
import com.ikang.staffapp.databinding.FragmentHomeBinding
import com.ikang.staffapp.ui.fragment.home.HomeListAdapter
import com.ikang.staffapp.ui.fragment.home.HomeViewModel
import com.ikang.staffapp.ui.login.LoginActivity
import com.ikang.staffapp.util.GlideImageLoader
import com.stx.xhb.androidx.XBanner
import kotlinx.android.synthetic.main.fragment_home.*


/*
    主界面  Bind 写法
 */
@Suppress("UNCHECKED_CAST")
class HomeFragment : BaseRefreshMoreFragment<HomeViewModel, FragmentHomeBinding>() {

    private val mAdapter by lazy { HomeListAdapter() }
    private var page: Int = 0
    private lateinit var banner: XBanner

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_home


    override fun initView(savedInstanceState: Bundle?) {
        bindSwipeRecycler(
            mSmartRefreshLayout,
            rv_home,
            mAdapter as BaseQuickAdapter<*, BaseViewHolder>
        )
        rv_home.addItemDecoration(DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL))

        mAdapter.apply {
            //banner
            banner = XBanner(context)
            banner.minimumWidth = android.view.ViewGroup.LayoutParams.MATCH_PARENT
            banner.layoutParams =
                android.view.ViewGroup.LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    360
                )
            banner.loadImage(GlideImageLoader())
            addHeaderView(banner)
        }

        observe(viewModel.mBanners) {
            banner.setBannerData(it)
        }

        observe(viewModel.projectData) {
            stopRefresh()
            if (it.curPage == 1) mAdapter.setNewData(it.datas)
            else mAdapter.addData(it.datas)
            page = it.curPage
        }
    }


    override fun onItemClick(
        adapter: BaseQuickAdapter<*, BaseViewHolder>,
        view: View,
        position: Int
    ) {

        ARouter.getInstance().build(RouterActPath.Login.NAV_LOGIN).navigation()

//        val intent = Intent(context, LoginActivity::class.java)
//        startActivity(intent)
    }

    override fun lazyLoadData() {
        viewModel.getHomeList(page)
        viewModel.getBanner()
    }

    override fun handleEvent(msg: Message) {
        when (msg.code) {
            HomeViewModel.navigationToLoginActivity -> {
                val intent = Intent().apply {
                    setClass(activity!!, LoginActivity::class.java)
                }
                startActivity(intent)
            }
        }
    }

    override fun onLoadMore() {
        page = 0
        viewModel.getHomeList(page)
    }

    /** 下拉刷新回调 */
    override fun onRefresh() {
        viewModel.getBanner()
        viewModel.getHomeList(page + 1)
    }
}
