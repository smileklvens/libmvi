package com.ikang.libmvi.base

/**
 * @author IK-zhulk
 * @version 1.0.0
 * @describe
 */
data class BaseResult<out T>( val code: Int,val errors: String,val message: String, val results: T) {
    fun isSuccess() = code == 1
}