package com.ikang.libmvi.util

import android.util.Log
import com.ikang.libmvi.config.ConfigLog

/**
 * @author ikang-renwei
 * @Date 2019/12/13 16:56
 * @describe Log工具类
 */
object XLog {
    var logSb: StringBuffer? = StringBuffer()
    var strLocation = ""
    private var TAG = "XLog"
    private var isOpenLog = false


    fun setLogTag(TAG: String) {
        XLog.TAG = TAG
    }

    fun setLogOpen(isOpenLog: Boolean) {
        XLog.isOpenLog = isOpenLog
    }

    fun d(tag: String, msg: String) {
        if (ConfigLog.LOG_OPEN || isOpenLog) {
            Log.d(tag, msg)
        }
    }

    fun e(tag: String, msg: String) {
        if (ConfigLog.LOG_OPEN || isOpenLog) {
            Log.e(tag, msg)
        }
    }

    fun d(msg: String) {
        if (ConfigLog.LOG_OPEN || isOpenLog) {
            Log.d(TAG, msg)
        }
    }

    fun e(msg: String) {
        if (ConfigLog.LOG_OPEN || isOpenLog) {
            Log.e(TAG, msg)
        }
    }

    fun appendLine() {
        if (ConfigLog.LOG_OPEN || isOpenLog) {
            if (null != logSb) {
                logSb!!.append("\r\n--------------------------------\r\n")
            }
        }
    }

    fun appendParams(mParams: String) {
        if (ConfigLog.LOG_OPEN || isOpenLog) {
            if (null != logSb) {
                logSb!!.append("入参:::\r\n$mParams")
            }
        }
    }

    fun appendUrl(targetUrl: String) {
        if (ConfigLog.LOG_OPEN || isOpenLog) {
            if (null != logSb) {
                logSb!!.append(targetUrl)
            }
        }
    }

    fun appendResp(resp: String) {
        if (ConfigLog.LOG_OPEN || isOpenLog) {
            if (null != logSb) {
                logSb!!.append("\r\n\r\n" + resp + "\r\n\r\n")
            }
        }
    }

    fun clearLogContent() {
        if (ConfigLog.LOG_OPEN || isOpenLog) {
            if (null != logSb) {
                logSb!!.setLength(0)
            }
        }

    }

}
