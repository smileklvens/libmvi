package com.kotlin.mall.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.ViewDataBinding
import com.ikang.libmvi.base.NoViewModel
import com.ikang.libmvi.base.ui.fragment.BaseFragment
import com.ikang.libmvi.util.ext.click
import com.ikang.staffapp.R
import com.ikang.staffapp.ui.login.LoginActivity
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
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }
    }

}
