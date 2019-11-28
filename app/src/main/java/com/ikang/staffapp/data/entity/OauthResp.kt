package com.ikang.staffapp.data.entity


/**
 * @author IK-zhulk
 * @version 1.0.0
 * @describe {@link #} /oauth2/token  接口结果
 */
data class OauthResp(
    val access_token: String,
    val error_code: String,
    val expires_in: Int,
    val member_id: String,
    val nickname: String,
    val refresh_token: String,
    val user_id: String
)