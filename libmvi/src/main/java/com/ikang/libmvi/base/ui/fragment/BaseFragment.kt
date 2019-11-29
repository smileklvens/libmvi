package com.ikang.libmvi.base.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aleyn.mvvm.event.Message
import com.ikang.libmvi.base.BaseViewModel
import com.ikang.libmvi.base.ui.IBaseView
import com.ikang.libmvi.base.ui.activity.BaseActivity
import java.lang.reflect.ParameterizedType

/**
 * @author IK-zhulk
 * @version 1.0.0
 * @describe Fragment父类，所有的Fragment都应该继承这个
 *
 *  //    val viewModel: LoginViewModule by viewModels()
private val mViewModel by lazy { createViewModel<LoginViewModule>() }

 */
abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding> : Fragment(), IBaseView {
    //是否第一次加载
    private var isFirst: Boolean = true
    var baseActivity: BaseActivity<*, *>? = null
    protected lateinit var viewModel: VM
    protected var mBinding: DB? = null

    open fun initView(savedInstanceState: Bundle?) {}

    @LayoutRes
    abstract fun layoutId(): Int


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val cls =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<*>
        if (ViewDataBinding::class.java != cls && ViewDataBinding::class.java.isAssignableFrom(cls)) {
            mBinding = DataBindingUtil.inflate(inflater, layoutId(), container, false)
            return mBinding?.root
        }
        return inflater.inflate(layoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onVisible()
        createViewModel()
        lifecycle.addObserver(viewModel)
        //注册 UI事件
        registorDefUIChange()
        initView(savedInstanceState)

    }


    override fun onResume() {
        super.onResume()
        onVisible()
    }


    /**
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            lazyLoadData()
            isFirst = false
        }
    }

    /**
     * 懒加载
     */
    open fun lazyLoadData() {}

    /**
     * 注册 UI 事件
     */
    private fun registorDefUIChange() {
        viewModel.defUI.showDialog.observe(viewLifecycleOwner, Observer {
            showLoading()
        })
        viewModel.defUI.dismissDialog.observe(viewLifecycleOwner, Observer {
            hideLoading()
        })
        viewModel.defUI.toastEvent.observe(viewLifecycleOwner, Observer {
            toast(it)
        })
        viewModel.defUI.msgEvent.observe(viewLifecycleOwner, Observer {
            handleEvent(it)
        })
    }

    open fun handleEvent(msg: Message) {}

    override fun onAttach(context: Context) {
        super.onAttach(context)
        baseActivity = context as? BaseActivity<*, *>
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


    /**
     * 创建 ViewModel
     */
    @Suppress("UNCHECKED_CAST")
    private fun createViewModel() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val tp = type.actualTypeArguments[0]
            val tClass = tp as? Class<VM> ?: BaseViewModel::class.java
            viewModel =
                ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(tClass) as VM
        }
    }

}
