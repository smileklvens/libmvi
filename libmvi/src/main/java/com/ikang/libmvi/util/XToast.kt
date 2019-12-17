package com.ikang.libmvi.util

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import com.ikang.libeasyshow.toasty.Toasty

/**
 * @author ikang-renwei
 * @Date 2019/12/13 17:00
 * @describe Toast工具类
 */
object XToast {
    private lateinit var toast: Toast
    private val LENGTH_DEFAULT: Int = Toast.LENGTH_SHORT

    fun cancel() {
        toast?.cancel()
    }

    fun show(context: Context,textId:Int){
        show(context,textId, LENGTH_DEFAULT)
    }

    fun show(context: Context,text:String){
        show(context,text, LENGTH_DEFAULT)
    }

    fun show(context: Context,textId: Int,duration:Int){
        val text = context.getString(textId)
        show(context, text, duration)
    }

    /**
     * 通用，吐司提示。
     * @param context
     * @param text
     * @param duration LENGTH_SHORT或LENGTH_LONG
     */
    fun show(context: Context, text: String, duration: Int) {
        toast = Toasty.normal(context, text, duration)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}