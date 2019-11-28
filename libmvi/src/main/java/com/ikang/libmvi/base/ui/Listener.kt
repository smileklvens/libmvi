package com.ikang.libmvi.base.ui

import android.view.View


/**
 * @author ikang-zhulk
 * @version 1.0.0
 * @describe {@link #}
 */
interface Listener : View.OnClickListener {

    override fun onClick(v: View?)
}