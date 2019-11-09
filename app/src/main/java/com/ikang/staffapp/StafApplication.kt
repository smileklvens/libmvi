package com.ikang.staffapp

import android.content.Context
import com.ikang.libmvi.base.BaseApp
import kotlin.properties.Delegates


/**
 * @author ikang-zhulk
 * @version 1.0.0
 * @describe {@link #}
 */
class StafApplication : BaseApp() {

    companion object {
        var instance: Context by Delegates.notNull()
            private set
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}