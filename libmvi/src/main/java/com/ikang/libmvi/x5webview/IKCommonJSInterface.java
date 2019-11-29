package com.ikang.libmvi.x5webview;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.ikang.libmvi.base.ui.activity.BaseActivity;

import java.lang.ref.WeakReference;

/**
 * @author IK-zhulk
 * @version 1.0.0
 * @describe {@link #} 通用js事件
 */
public class IKCommonJSInterface {


    WeakReference<BaseActivity> mActivityWeakReference;

    public IKCommonJSInterface(BaseActivity baseActivity) {
        mActivityWeakReference = new WeakReference<>(baseActivity);
    }

    /**
     * 打印H5页面的源码
     *
     * @param html H5页面源码
     */
    @JavascriptInterface
    public void showSource(String html) {
        Log.e("HTML====>>>", html);
    }

    @JavascriptInterface
    public void onBtnLeftClick() {
        if (null != mActivityWeakReference.get()) {
            mActivityWeakReference.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mActivityWeakReference.get().finish();
                }
            });
        }
    }

}
