package com.ikang.libmvi.base.viewmodel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.example.github.vo.ViewStatus
import com.ikang.libmvi.base.vo.BaseResp
import com.ikang.libmvi.base.http.ExceptionHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    //异常数据
    val mViewStatus: MutableLiveData<ViewStatus> = MutableLiveData()

    private fun <T> runOnTryCatch(
        request: suspend CoroutineScope.() -> BaseResp<T>,
        success: ((info: T) -> Unit)
    ) {
        viewModelScope.launch {
            mViewStatus.value = ViewStatus.loading()
            try {
                val response = request()
//                    withContext(Dispatchers.IO) {
//                    request()
//                    }
                if (response.isSuccess) {
                    success.invoke(response.results)
                    mViewStatus.value = ViewStatus.success()
                } else {
                    mViewStatus.value = ViewStatus.error(response.message)
                }
            } catch (e: Exception) {
                var message = ExceptionHandle.handleException(e)?.message
                if (TextUtils.isEmpty(message)) {
                    message = "未知异常"
                }
                mViewStatus.value = ViewStatus.error(message!!)
            }
        }
    }


    fun <T> request(
        request: suspend CoroutineScope.() -> BaseResp<T>,
        success: ((info: T) -> Unit)
    ) {
        runOnTryCatch(request, success)
    }
}