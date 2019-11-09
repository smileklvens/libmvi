package com.ikang.libmvi.base.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.example.github.vo.ViewStatus
import com.ikang.libmvi.base.ext.observe
import com.ikang.libmvi.base.ui.IBaseView
import com.ikang.libmvi.base.ui.activity.BaseActivity
import com.ikang.libmvi.base.viewmodel.BaseViewModel

/**
 * @author IK-zhulk
 * @version 1.0.0
 * @describe Fragment父类，所有的Fragment都应该继承这个
 */
abstract class BaseFragment<VB : ViewDataBinding> : Fragment(), IBaseView {

    var baseActivity: BaseActivity<*>? = null

    // 布局view
    lateinit var mBindingView: VB

    @LayoutRes
    abstract fun layoutId(): Int

    open fun initData(savedInstanceState: Bundle?) {}

    inline fun <reified VM : BaseViewModel> createViewModel(): VM {
        val mViewModel = ViewModelProvider(this)[VM::class.java]
        observe(mViewModel.mViewStatus) {
            hideLoading()
            if (!TextUtils.isEmpty(it.message)) onFail(it)
        }
        return mViewModel
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBindingView = DataBindingUtil.inflate(inflater, layoutId(), container, false)
        return mBindingView.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mBindingView.lifecycleOwner = viewLifecycleOwner

        initData(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        baseActivity = context as? BaseActivity<*>
    }


    override fun onDetach() {
        super.onDetach()
        baseActivity = null
    }

    fun toast(str: String) {
        baseActivity?.toast(str)
    }


    override fun showLoading() {
        baseActivity?.showLoading()

    }

    override fun hideLoading() {
        baseActivity?.hideLoading()
    }


    open fun onFail(it: ViewStatus) {
        toast(it.message)
    }


    protected fun <T> autoWired(key: String, default: T? = null): T? =
        arguments?.let { findWired(it, key, default) }

    private fun <T> findWired(bundle: Bundle, key: String, default: T? = null): T? {
        return if (bundle.get(key) != null) {
            try {
                bundle.get(key) as T
            } catch (e: ClassCastException) {
                e.printStackTrace()
                null
            }
        } else default
    }


}
