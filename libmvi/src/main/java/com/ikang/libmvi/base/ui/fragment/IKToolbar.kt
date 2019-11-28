package com.ikang.libmvi.base.ui.fragment

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.ikang.libmvi.R

/**
 * @author ikang-zhulk
 * @version 1.0.0
 * @describe [.]
 */
class IKToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : Toolbar(context, attrs, defStyleAttr) {
    /**
     * 左侧Title
     */
    private var mTxtLeftTitle: TextView? = null
    /**
     * 中间Title
     */
    private var mTxtMiddleTitle: TextView? = null
    /**
     * 右侧Title
     */
    private var mTxtRightTitle: TextView? = null

    init {
        init(context)
    }

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.layout_simple_toolbar, this)
        mTxtLeftTitle = findViewById<View>(R.id.txt_left_title) as TextView
        mTxtMiddleTitle = findViewById<View>(R.id.txt_main_title) as TextView
        mTxtRightTitle = findViewById<View>(R.id.txt_right_title) as TextView
    }

    //设置中间title的内容
    fun setMainTitle(text: CharSequence?) {
        this.title = " "
        mTxtMiddleTitle!!.visibility = View.VISIBLE
        mTxtMiddleTitle!!.text = text
    }

    //设置中间title的内容文字的颜色
    fun setMainTitleColor(color: Int) {
        mTxtMiddleTitle!!.setTextColor(color)
    }

    //设置title左边文字
    fun setLeftTitleText(text: CharSequence?) {
        mTxtLeftTitle!!.visibility = View.VISIBLE
        mTxtLeftTitle!!.text = text
    }

    //设置title左边文字颜色
    fun setLeftTitleColor(color: Int) {
        mTxtLeftTitle!!.setTextColor(color)
    }

    //设置title左边图标
    fun setLeftTitleDrawable(res: Int) {
        val dwLeft = ContextCompat.getDrawable(context, res)
        dwLeft!!.setBounds(0, 0, dwLeft.minimumWidth, dwLeft.minimumHeight)
        mTxtLeftTitle!!.setCompoundDrawables(dwLeft, null, null, null)
    }

    //设置title左边点击事件
    fun setLeftTitleClickListener(onClickListener: OnClickListener?) {
        mTxtLeftTitle!!.setOnClickListener(onClickListener)
    }

    //设置title右边文字
    fun setRightTitleText(text: String?) {
        mTxtRightTitle!!.visibility = View.VISIBLE
        mTxtRightTitle!!.text = text
    }

    //设置title右边文字颜色
    fun setRightTitleColor(color: Int) {
        mTxtRightTitle!!.setTextColor(color)
    }

    //设置title右边图标
    fun setRightTitleDrawable(res: Int) {
        val dwRight = ContextCompat.getDrawable(context, res)
        dwRight!!.setBounds(0, 0, dwRight.minimumWidth, dwRight.minimumHeight)
        mTxtRightTitle!!.setCompoundDrawables(null, null, dwRight, null)
    }

    //设置title右边点击事件
    fun setRightTitleClickListener(onClickListener: OnClickListener?) {
        mTxtRightTitle!!.setOnClickListener(onClickListener)
    }


}