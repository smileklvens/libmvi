package com.ikang.libmvi.base.ui

import android.view.View
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseViewHolder
import com.ikang.libmvi.R

/**
 *   @auther : Aleyn
 *   time   : 2019/09/04
 */
@Suppress("UNCHECKED_CAST")
class BaseDBViewHoder<BD : ViewDataBinding>(val view: View) : BaseViewHolder(view) {

    fun getBinding(): BD {
        return view.getTag(R.id.BaseQuickAdapter_databinding_support) as BD
    }

}