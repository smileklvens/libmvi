package com.kotlin.mall.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.databinding.ViewDataBinding
import com.ikang.libmvi.base.NoViewModel
import com.ikang.libmvi.base.ui.fragment.BaseFragment
import com.ikang.libmvi.util.ext.click
import com.ikang.mymodule.R
import com.ikang.providerservice.service.login.LoginServiceControl
import kotlinx.android.synthetic.main.fragment_me.*


/*
   一般写法
 */
class MeFragment : BaseFragment<NoViewModel, ViewDataBinding>() {
    override fun layoutId(): Int = R.layout.fragment_me

    override fun lazyLoadData() {
        Log.d("MeFragment", "ggg")
    }

    override fun initView(savedInstanceState: Bundle?) {
        tv.click {
//            val intent = Intent(context, LoginActivity::class.java)
//            startActivity(intent)
        }

        tv_quit.click {
            toast("lalala")
        }
        tv_quit.text = LoginServiceControl.accountService.accountId
    }

}
