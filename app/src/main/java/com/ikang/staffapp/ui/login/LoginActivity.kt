/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ikang.staffapp.ui.login

import android.os.Bundle
import com.ikang.libmvi.base.NoViewModel
import com.ikang.libmvi.base.ui.activity.BaseActivity
import com.ikang.libmvi.util.ext.click
import com.ikang.staffapp.R
import com.ikang.staffapp.databinding.ActivityLoginBinding
import com.ikang.staffapp.widget.EdittextClearCombination
import kotlinx.android.synthetic.main.activity_login.*

/**
 * 如果不需要自己定义ViewModel，可以使用公共的NoViewModel
 */
class LoginActivity : BaseActivity<NoViewModel,ActivityLoginBinding>(), EdittextClearCombination.ITextStandardListener  {

    //当前手机号是否合法
    private var telIsStandard = false

    override fun layoutId(): Int = R.layout.activity_login


    override fun initView(savedInstanceState: Bundle?) {
//        supportFragmentManager
//            .beginTransaction()
//            .replace(R.id.container, LoginFragment.newInstance())
//            .commitNow()
//
//        setToolBar("ff")

        //手机格式化回调监听
        loginPhoneNumberEdt.setTextStandardListener(this)

        loginBtnLogin.click {
            if (telIsStandard){
                toast("点击登录")
            }
        }
    }

    override fun initData() {

    }

    //当输入手机号格式回调
    override fun textStandard(visible: Boolean) {
        telIsStandard = visible
        //改变获取验证码按钮颜色
        if (visible) {
            loginBtnLogin.setBackgroundResource(R.drawable.btn_red_big_corner)
        } else {
            loginBtnLogin.setBackgroundResource(R.drawable.btn_gray_big_corner)
        }
    }
}
