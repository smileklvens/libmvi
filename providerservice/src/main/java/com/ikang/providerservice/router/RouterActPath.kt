package com.ikang.providerservice.router

/**
 * @author ikang-zhulk
 * @version 1.0.0
 * @describe 路由地址的命名规则为 组件名 + 页面名, 如订单组件的订单详情页的路由地址可以命名为 "/order/OrderDetailActivity"
 */
class RouterActPath {
    /**
     * 登录组件
     */
    object Login {
        const val LOGIN = "/login"
        /*登录界面*/
        const val NAV_LOGIN = "$LOGIN/LoginActivity"
    }

    /**
     * 用户组件
     */
    object User {
        const val USER = "/my"
        /*用户详情*/
        const val PAGER_USERDETAIL =
            "$USER/AccountSafetyActivity"
    }
}