package com.ikang.staffapp.http.interceptor

import android.text.TextUtils
import com.aleyn.mvvm.network.ResERROR
import com.aleyn.mvvm.network.ResponseThrowable
import com.google.gson.Gson
import com.ikang.libmvi.base.BaseResult
import com.ikang.staffapp.data.entity.OauthResp
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
/**
 * @author IK-zhulk
 * @version 1.0.0  {"code":0,"errors":"","isSuccess":false,"message":"","results":{"access_token":"d045fcb47c92fda1e135b6fa50d8d166","error_code":"0","expires_in":7776000,"member_id":"38733487","nickname":"xingikang","refresh_token":"53c39d538a2cabd27868e646f163f27a","user_id":"1176506"}}
 * @describe {@link #}后台分布式模块返回数据，太乱了，统一处理,😔😔😔
 */
class IKGlobalIntercept : ResponseBodyInterceptor() {
    override fun intercept(response: Response, url: String, body: String): Response {
        if (TextUtils.isEmpty(body) || "null".equals(body, ignoreCase = true)) {
            throw ResponseThrowable(ResERROR.NETWORD_ERROR)
        }

        try {
            if (url.contains("/oauth2/token")) { // 当请求的是登录接口才转换
                val oauthResp = Gson().fromJson(body, OauthResp::class.java)
                val toJson = Gson().toJson(
                    BaseResult<OauthResp>(
                        1,
                        "",
                        "操作成功",
                        oauthResp
                    )
                )
                val contentType = response.body?.contentType()
                val responseBody = toJson.toResponseBody(contentType)
                return response.newBuilder().body(responseBody).build() // 重新生成响应对象
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }
}