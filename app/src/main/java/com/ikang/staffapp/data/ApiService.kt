package com.ikang.staffapp.http

import com.ikang.libmvi.base.vo.BaseResp
import com.ikang.staffapp.data.vo.OauthResp
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


/**
 * @author ikang-zhulk
 * @version 1.0.0
 * @describe {@link #}
 */
const val BASE_URL = "https://newuat-oauth2.health.ikang.com"

interface ApiService {

    /*统一登录平台获取 access_token*/
    @FormUrlEncoded
    @POST("/oauth2/token")
    suspend fun getLoginToken(@FieldMap formMap: Map<String, String>): BaseResp<OauthResp>

    /*统一登录平台 session*/
    @GET("/api/login")
    suspend fun getLoginSession(@FieldMap formMap: Map<String, String>): BaseResp<OauthResp>

}