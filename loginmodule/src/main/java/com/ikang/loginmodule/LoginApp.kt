package com.ikang.loginmodule

import android.content.Context
import com.ikang.providerservice.delegate.IApp
import com.ikang.providerservice.service.login.LoginServiceControl


/**
 * @author ikang-zhulk
 * @version 1.0.0
 * @describe {@link #}
 */
class LoginApp: IApp {
    override fun attachBaseContext(base: Context) {
    }

    override fun onCreate(base: Context) {
        LoginServiceControl.accountService = LoginServiceImpl()
    }

    override fun onTerminate(base: Context) {
    }

}