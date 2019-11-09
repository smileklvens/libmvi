package com.lai.comicmtc_v2.ui.widget.dialog

import android.content.Context
import android.view.View
import com.ikang.libglide.GlideImageLoader
import com.ikang.libmvi.R
import kotlinx.android.synthetic.main.layout_loading.*

/**
 * @author IK-zhulk
 * @version 1.0.0
 * @describe
 *
 */
class LoadingDialog(context: Context) : BaseDialog(context, R.style.LoadingDialog) {
    override fun getLayout(): Int {
        return R.layout.layout_loading
    }

    override fun init(view: View) {
        GlideImageLoader.create(animationView).loadDrawable(R.drawable.basic_loading_wait_dialog)
    }

    override fun dismiss() {
        super.dismiss()
    }


}