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
import com.ikang.staffapp.R
import com.ikang.staffapp.databinding.ActivityLoginBinding

/**
 * 如果不需要自己定义ViewModel，可以使用公共的NoViewModel
 */
class LoginActivity : BaseActivity<NoViewModel,ActivityLoginBinding>() {

    override fun layoutId(): Int = R.layout.activity_login


    override fun initView(savedInstanceState: Bundle?) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, LoginFragment.newInstance())
            .commitNow()

        setToolBar("ff")
    }

    override fun initData() {

    }
}
