package com.ikang.staffapp.http

import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.ikang.libmvi.BuildConfig
import com.ikang.libmvi.base.BaseApp
import com.ikang.staffapp.http.constant.HttpConstant
import com.ikang.staffapp.http.interceptor.CacheInterceptor
import com.ikang.staffapp.http.interceptor.HeaderInterceptor
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * @author ikang-zhulk
 * @version 1.0.0
 * @describe {@link #}
 */
object RetrofitFactory {

    fun <S> getService(serviceClass: Class<S>, baseUrl: String): S {
        return getService(serviceClass, baseUrl, CacheInterceptor())
    }

    fun <S> getService(serviceClass: Class<S>, baseUrl: String, interceptor: Interceptor): S {
        return Retrofit.Builder()
            .client(getOkHttpClient(interceptor))
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build().create(serviceClass)
    }


    /**
     * 获取 OkHttpClient
     */
    private fun getOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val builder = OkHttpClient().newBuilder()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        //设置 请求的缓存的大小跟位置
        val cacheFile = File(BaseApp.instance.cacheDir, "cache")
        val cache = Cache(cacheFile, HttpConstant.MAX_CACHE_SIZE)

        builder.run {
            addNetworkInterceptor(httpLoggingInterceptor)
            addInterceptor(HeaderInterceptor())
            addInterceptor(interceptor)

            cache(cache)  //添加缓存
            cookieJar(cookieJar)
            connectTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true) // 错误重连
            // cookieJar(CookieManager())
        }
        return builder.build()
    }


    private val cookieJar by lazy {
        PersistentCookieJar(
            SetCookieCache(), SharedPrefsCookiePersistor(
                BaseApp.instance
            )
        )
    }

}