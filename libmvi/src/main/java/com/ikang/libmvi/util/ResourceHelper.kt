package com.ikang.libmvi.util

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.ikang.libmvi.base.BaseApp


/**
 * @author ikang-zhulk
 * @version 1.0.0
 * @describe {@link #}
 */
class ResourceHelper {
    private fun ResourceHelper() {
        throw UnsupportedOperationException("ResourceHelper can't  be  instantiate ")
    }
    companion object{
        /**
         * @return  获取上下文对象
         */
        fun getContext(): Context {
            return BaseApp.instance
        }

        /**
         *
         * @return  获取资源Android资源
         */
        fun getResource(): Resources {
            return getContext().resources
        }

        /**
         *
         * @param resId
         * @return
         */
        fun getString(resId: Int): String? {
            return getResource().getString(resId)
        }

        /**
         *
         * @param resId
         * @return
         */
        fun getDrawable(resId: Int): Drawable? {
            return ContextCompat.getDrawable(getContext(), resId)
        }


        /**
         *
         * @param resId
         * @return
         */
        fun getColor(resId: Int): Int {
            return ContextCompat.getColor(getContext(), resId)
        }

        /**
         *
         * @param resId
         * @return
         */
        fun getDimens(resId: Int): Float {
            return getResource().getDimension(resId)
        }


        //========  ========
        private val RES_ID = "id"
        private val RES_STRING = "string"
        private val RES_DRAWABLE = "drawable"
        private val RES_LAYOUT = "layout"

        /**
         * 获取资源文件的id
         *
         * @param resName
         * @return
         */
        fun getId(resName: String?): Int {
            return getResId(resName, RES_ID)
        }

        /**
         * 获取资源文件string的id
         *
         * @param resName
         * @return
         */
        fun getStringId(resName: String?): Int {
            return getResId(resName, RES_STRING)
        }

        /**
         * 获取资源文件drawable的id
         *
         * @param resName
         * @return
         */
        fun getDrawableId(resName: String?): Int {
            return getResId(resName, RES_DRAWABLE)
        }

        /**
         * 获取资源文件layout的id
         *
         * @param resName
         * @return
         */
        fun getLayoutId(resName: String?): Int {
            return getResId(resName, RES_LAYOUT)
        }


        /**
         * 获取资源文件ID
         *
         * @param resName
         * @param defType
         * @return
         */
        fun getResId(resName: String?, defType: String?): Int {
            return getResource().getIdentifier(resName, defType, getContext().packageName)
        }
    }



}