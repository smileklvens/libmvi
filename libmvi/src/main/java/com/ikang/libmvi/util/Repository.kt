package com.ikang.libmvi.util

import android.content.Context
import android.content.SharedPreferences
import com.ikang.libmvi.base.BaseApp
import com.ikang.libmvi.util.ext.gson
import com.ikang.libmvi.util.ext.initKey
import com.ikang.libmvi.util.ext.string


/**
 * @author IK-zhulk
 * @version 1.0.0
 * @describe {@link #}
 */
const val TJB_PREFS_NAME = "TJB_PREFS"

object Repository {

    val TJBPrefs by lazy {
        PrefsHelper(
            BaseApp.instance.getSharedPreferences(
                TJB_PREFS_NAME,
                Context.MODE_PRIVATE
            )
        )
    }

}


const val  KEY_TOKEN = "token"

class PrefsHelper(prefs: SharedPreferences) {

    init {
        prefs.initKey("12345678910abcde") // 初始化密钥，且密钥是16位的
    }

    /*加密*/
    var token by prefs.string(KEY_TOKEN, "", isEncrypt = true)

    /*不加密*/
    var age by prefs.string("age")


    var user2 by prefs.gson<Any?>(null)
}