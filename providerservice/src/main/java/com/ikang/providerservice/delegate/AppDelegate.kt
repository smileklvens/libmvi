package com.ikang.providerservice.delegate

import android.content.Context

/**
 * @author ikang-zhulk
 * @version 1.0.0
 * @describe 代理application生命周期
 */
class AppDelegate(context: Context) : IApp {

    var mAppLifes: List<IApp> = CommonManifestParser(context).parse()


    override fun attachBaseContext(base: Context) {
        for (module in mAppLifes) {
            module.attachBaseContext(base)
        }
    }

    override fun onCreate(base: Context) {
        for (module in mAppLifes) {
            module.onCreate(base)
        }
    }

    override fun onTerminate(base: Context) {
        for (module in mAppLifes) {
            module.onTerminate(base)
        }
    }


}