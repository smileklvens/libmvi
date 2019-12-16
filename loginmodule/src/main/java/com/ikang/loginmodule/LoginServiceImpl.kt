package com.ikang.loginmodule

import com.ikang.providerservice.service.login.ILoginService


/**
 * @author ikang-zhulk
 * @version 1.0.0
 * @describe {@link #}
 */
class LoginServiceImpl : ILoginService {
    override val isLogin: Boolean = true

    override val accountId: String = "张三"
}