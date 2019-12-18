package com.ikang.libmvi.base

import android.app.Application
import android.content.Context
import android.os.StrictMode
import android.util.Log
import androidx.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.ikang.libmvi.BuildConfig
import com.ikang.providerservice.delegate.AppDelegate
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback
import kotlin.properties.Delegates


/**
 * @author ikang-zhulk
 * @version 1.0.0
 * @describe {@link #} 需要注册AppComponent中的application组件
 */
open class BaseApp : Application() {

    companion object {
        var instance: Application by Delegates.notNull()
            private set
    }

    val mAppDelegate: AppDelegate by lazy { AppDelegate(this) }

    override fun onCreate() {
        super.onCreate()
        instance = this

//        if (BuildConfig.DEBUG) {
//            enableStrictMode()
//        }

        initArouter()

        mAppDelegate.onCreate(this)


        initX5WebView()
    }

    private fun initArouter() {
        if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
    }

    override fun onTerminate() {
        super.onTerminate()
        mAppDelegate.onTerminate(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        mAppDelegate.attachBaseContext(this)

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

    fun initX5WebView() { //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        val cb: PreInitCallback = object : PreInitCallback {
            override fun onViewInitFinished(arg0: Boolean) { //x5內核初始化完成的回调，为true表示x5内核加载成功
//否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is $arg0")
            }

            override fun onCoreInitFinished() {
                Log.d("app", " onCoreInitFinished ")
            }
        }
        //x5内核初始化接口
        QbSdk.initX5Environment(this, cb)
    }



}