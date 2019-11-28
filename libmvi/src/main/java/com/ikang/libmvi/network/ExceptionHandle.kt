package com.aleyn.mvvm.network

import android.net.ParseException
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException

/**
 *   @auther : Aleyn
 *   time   : 2019/08/12
 */
object ExceptionHandle {

    fun handleException(e: Throwable): ResponseThrowable {
        val ex: ResponseThrowable
        if (e is HttpException) {
            ex = ResponseThrowable(ResERROR.NETWORD_ERROR)
            return ex
        } else if (e is JsonParseException
            || e is JSONException
            || e is ParseException || e is MalformedJsonException
        ) {
            ex = ResponseThrowable(ResERROR.PARSE_ERROR)
            return ex
        } else if (e is ConnectException) {
            ex = ResponseThrowable(ResERROR.NETWORD_ERROR)
            return ex
        } else if (e is javax.net.ssl.SSLException) {
            ex = ResponseThrowable(ResERROR.SSL_ERROR)
            return ex
        } else if (e is ConnectTimeoutException) {
            ex = ResponseThrowable(ResERROR.TIMEOUT_ERROR)
            return ex
        } else if (e is java.net.SocketTimeoutException) {
            ex = ResponseThrowable(ResERROR.TIMEOUT_ERROR)
            return ex
        } else if (e is java.net.UnknownHostException) {
            ex = ResponseThrowable(ResERROR.TIMEOUT_ERROR)
            return ex
        } else {
            ex = ResponseThrowable(ResERROR.UNKNOWN)
            return ex
        }
    }
}