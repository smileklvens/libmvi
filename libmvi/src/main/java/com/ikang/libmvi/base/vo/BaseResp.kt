package com.ikang.libmvi.base.vo


import android.text.TextUtils

/**
 * 项目名称：体检宝
 * 描述：数据返回格式
 * 创建人：ZCG
 * 创建时间：2017/6/28
 * 邮箱：bravebigboy@163.com
 */

data class BaseResp<T>(
    val code: Int,
    val errors: String,
    val message: String,
    var results: T
) {

    val isSuccess = (code == 1)
}
