package com.ikang.libmvi.base.ui.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.android.example.github.vo.Status
import com.android.example.github.vo.ViewStatus
import com.ikang.libglide.GlideImageLoader
import com.ikang.libmvi.R
import com.ikang.libmvi.base.BaseApp
import com.ikang.libmvi.base.ext.observe
import com.ikang.libmvi.base.ui.IBaseView
import com.ikang.libmvi.base.util.StatusBarUtil
import com.ikang.libmvi.base.viewmodel.BaseViewModel
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.layout_loading_view.*


/**
 * @author IK-zhulk
 * @version 1.0.0
 * @describe Activity父类，所有的activity都应该继承这个
 *           <p>
 *              1、如果需要videmodule 就调用     private val mViewModel by lazy { createViewModel<SearchViewModel>() }
 *              2、协程lifecycleScope.launchWhenStarted
 *           </p>
 *
 */
abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity(), IBaseView {

//    lateinit var mLoadingDialog: LoadingDialog

    // 布局view
    lateinit var mBindingView: VB
    var errorView: View? = null
    var loadingView: View? = null

    @LayoutRes
    abstract fun layoutId(): Int

    open fun initData(savedInstanceState: Bundle?) {

    }

    /**
     * 获取ViewModel，并监听视图，加载中和失败的回调
     */
    inline fun <reified VM : BaseViewModel> createViewModel(): VM {
        val mViewModel = ViewModelProvider(this)[VM::class.java]
        observe(mViewModel.mViewStatus) {
            when (it.status) {
                Status.LOADING -> {
                    showLoading()
                }
                Status.SUCCESS -> {
                    hideLoading()
                }
                Status.ERROR -> {
                    hideLoading()
                    if (!TextUtils.isEmpty(it.message)) onFail(it)
                }
            }
        }
        return mViewModel
    }

    open fun onFail(it: ViewStatus) {
        toast(it.message)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dispatchContentView()

        handleStatusBar()

        initData(savedInstanceState)

        //        initViewModel()
    }

    /**
     * 处理content 和 titleBar
     */
    private fun dispatchContentView() {
        setContentView(R.layout.activity_base)
        mBindingView = DataBindingUtil.inflate(layoutInflater, layoutId(), mContainer, false)
        mContainer.addView(mBindingView.root)

        setToolBar(resources.getString(R.string.app_name), true, mToolBar)


    }


    protected fun setToolBar(
        title: CharSequence,
        hasTitleBar: Boolean = true,
        toolbar: Toolbar = mToolBar

    ) {

        if (hasTitleBar) {
            toolbar.visibility = View.VISIBLE
            setSupportActionBar(toolbar)
            val actionBar = supportActionBar

            actionBar?.run {
                setDisplayShowTitleEnabled(false)
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.icon_back)
            }

            toolbar.run {
                setNavigationOnClickListener { supportFinishAfterTransition() }
                setTitle(title)
            }
        } else {
            toolbar.visibility = View.GONE
        }
    }


    private fun handleStatusBar() {
        setStatusBarColor(resources.getColor(R.color.baseColorAccent))
        setStatusBarIcon(false)

    }

    /**
     * 设置状态栏的背景颜色
     */
    fun setStatusBarColor(@ColorInt color: Int) {
        StatusBarUtil.setColor(this, color, 0)
    }

    /**
     * 设置状态栏图标的颜色
     *
     * @param dark true: 黑色  false: 白色
     */
    fun setStatusBarIcon(dark: Boolean) {
        if (dark) {
            StatusBarUtil.setLightMode(this)
        } else {
            StatusBarUtil.setDarkMode(this)
        }
    }

    override fun hideLoading() {
        loadingView?.visibility = View.GONE
    }

    override fun showLoading() {
        if (this.isFinishing || this.isDestroyed) {
            return
        }

        if (loadingView == null) {
            loadingView = vsLoading.inflate()
        }
        if (loadingView?.visibility != View.VISIBLE) {
            loadingView?.visibility = View.VISIBLE
            GlideImageLoader.create(img_progress).loadDrawable(R.drawable.basic_loading_wait_dialog)
        }
        errorView?.visibility = View.GONE
    }

    protected fun showError() {
        if (loadingView?.visibility != View.GONE) {
            loadingView?.visibility = View.GONE
        }

        if (errorView == null) {
            errorView = vsError.inflate()
            // 点击加载失败布局
            errorView?.setOnClickListener {
                showLoading()
                onRefresh()
            }
        } else {
            errorView?.visibility = View.VISIBLE
        }

        mBindingView.root.visibility = View.GONE
    }


    /**
     * 失败后点击刷新
     */
    protected fun onRefresh() {
    }


    fun toast(str: String) {
        Toast.makeText(BaseApp.instance, str, Toast.LENGTH_LONG).show()
    }
}