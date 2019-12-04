package com.ikang.libutils.utils

import java.util.regex.Pattern

/**
 * @author ikang-renwei
 * @Date 2019/12/03 17:13
 * @describe
 */
object StringUtils {
    fun checkMobile(mobile: String): Boolean {
        val p = Pattern.compile("^(1)\\d{10}$")
        val matcher = p.matcher(mobile)
        return matcher.matches()
    }
}