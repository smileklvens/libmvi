package com.ikang.libmvi.base

import android.app.Application
import android.content.Context
import android.os.StrictMode
import androidx.multidex.MultiDex
import com.ikang.libmvi.BuildConfig
import kotlin.properties.Delegates


/**
 * @author ikang-zhulk
 * @version 1.0.0
 * @describe {@link #} 需要注册AppComponent中的application组件
 */
open class BaseApp : Application() {


    override fun onCreate() {
//        if (BuildConfig.DEBUG) {
//            enableStrictMode()
//        }
        super.onCreate()
        instance = this

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }


    private fun enableStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build()
        )
    }

    companion object {
        var instance: Context by Delegates.notNull()
            private set
    }
}