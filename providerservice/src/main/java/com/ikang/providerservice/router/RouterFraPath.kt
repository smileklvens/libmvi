package com.ikang.providerservice.router

/**
 * @author ikang-zhulk
 * @version 1.0.0
 * @describe 路由地址的命名规则为 组件名 + 页面名
 */
class RouterFraPath {
    /**
     * 用户组件
     */
    object User {
        private const val USER = "/my"
        /*我的*/
        const val PAGER_ME =
            "$USER/Me"
    }
}