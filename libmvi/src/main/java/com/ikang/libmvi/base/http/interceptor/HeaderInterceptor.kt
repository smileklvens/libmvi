package com.ikang.staffapp.http.interceptor

import android.os.Build
import android.util.Base64
import com.cxz.kotlin.baselibs.utils.AppUtils
import com.ikang.libmvi.base.BaseApp
import com.ikang.libmvi.base.util.KEY_TOKEN
import com.ikang.libmvi.base.util.Repository
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

/**
 * @author ikang-zhulk
 * @version 1.0.0
 * @describe {@link #}
 */
const val CLIENT_ID = "2f6cebf8-ed1e-11e5-b7ad-acbc32acde63"
const val CLIENT_SECRET = "1ee28ee1-3036-4828-a98a-c12a053b5bde"


val systemVersion = Build.VERSION.SDK_INT.toString()
val vender = Build.MANUFACTURER + Build.MODEL
val appVersion = AppUtils.getVerName(BaseApp.instance)
val authorization = "Basic " + Base64.encodeToString(
    ("$CLIENT_ID:$CLIENT_SECRET").toByteArray(),
    Base64.NO_WRAP
)

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

        builder
            .addHeader("platform", "android")
            .addHeader("clientType", "android")
            .addHeader("channelPackage", "default")
            .addHeader("comefrom", "36")

            .addHeader("systemVersion", systemVersion)
            .addHeader("vender", vender)
            .addHeader("appVersion", appVersion)
            .addHeader("Authorization", authorization)
            .addHeader("time", Calendar.getInstance().timeInMillis.toString())
            .addHeader(KEY_TOKEN, Repository.TJBPrefs.token)

        return chain.proceed(builder.build())
    }
}