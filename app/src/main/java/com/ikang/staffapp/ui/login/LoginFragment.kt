package com.ikang.staffapp.ui.login

import android.os.Bundle
import android.util.Log
import com.ikang.libmvi.base.ext.observe
import com.ikang.libmvi.base.ui.fragment.BaseRefreshFragment
import com.ikang.staffapp.R
import com.ikang.staffapp.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.fragment_login.*


/**
 * @author IK-zhulk
 * @version 1.0.0
 * @describe [.]
 */
class LoginFragment : BaseRefreshFragment<FragmentLoginBinding>() {

    override fun onRefresh() {
        mViewModel.getLoginToken()
    }

    //    val viewModel: LoginViewModule by viewModels()
    private val mViewModel by lazy { createViewModel<LoginViewModule>() }


    override fun loadData() {
        bindRefreshLayout(mSmartRefreshLayout)
        mViewModel.getLoginToken()
    }

    override fun layoutId(): Int {
        return R.layout.fragment_login
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        observe(mViewModel.mComicDetailResponse) {
            Log.i("token",it.access_token)

            mViewModel.getLoginSession(mapOf())
        }

        tv.setOnClickListener {
            mViewModel.getLoginToken()
        }
    }
}
