package com.ikang.staffapp.ui.login

import android.os.Bundle
import android.util.Log
import com.ikang.libmvi.base.ui.fragment.BaseRefreshFragment
import com.ikang.libmvi.util.ext.observe
import com.ikang.staffapp.R
import com.ikang.staffapp.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.fragment_login.*


/**
 * @author IK-zhulk
 * @version 1.0.0
 * @describe [.]
 */
class LoginFragment : BaseRefreshFragment<LoginViewModule,FragmentLoginBinding>() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun onRefresh() {
        viewModel.getLoginToken()
    }


    override fun initView(savedInstanceState: Bundle?) {
        bindRefreshLayout(mSmartRefreshLayout)
        observe(viewModel.mComicDetailResponse) {
                        Log.i("token",it.access_token)

            viewModel.getLoginSession()
        }

        tv.setOnClickListener {
            viewModel.getLoginToken()
        }

    }


    override fun lazyLoadData() {
        viewModel.getLoginToken()
    }


    override fun layoutId(): Int {
        return R.layout.fragment_login
    }


}
