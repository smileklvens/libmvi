package com.ikang.providerservice.service.login

import com.ikang.providerservice.service.IService


/**
 * 登陆组件 暴露方法
 */
interface ILoginService : IService {
    val isLogin: Boolean
    val accountId: String
}