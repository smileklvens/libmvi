package com.ikang.providerservice.delegate

import android.content.Context

/**
 * @author ikang-zhulk
 * @version 1.0.0
 * @describe 模块初始化接口
 */
interface IApp {

    fun attachBaseContext(base: Context)

    fun onCreate(base: Context)

    fun onTerminate(base: Context)
}