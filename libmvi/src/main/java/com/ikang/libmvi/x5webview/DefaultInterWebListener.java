package com.ikang.libmvi.x5webview;

import androidx.annotation.IntRange;

import com.tencent.smtt.sdk.WebView;


/**
 * @author IK-zhulk
 * @version 1.0.0
 * @describe {@link #}
 */
public  class DefaultInterWebListener  implements  InterWebListener{

    /**
     * 进度条变化时调用
     *
     * @param newProgress 进度0-100
     */
    public void startProgress(@IntRange(from = 0, to = 100) int newProgress) {

    }

    /**
     * 隐藏进度条
     */
    public void hindProgressBar() {
    }


    /**
     * 返回标题处理
     */
    public void onReceivedTitle(WebView view, String title) {
    }

    /**
     * 展示异常页面
     */
    public void showErrorView() {
    }


    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
    }

    /**
     * 页面加载结束，添加js监听等
     */
    public void onPageFinished(WebView view, String url) {
    }

    /**
     * 是否处理打开三方app
     *
     * @param url
     */
    public boolean isOpenThirdApp(WebView view, String url) {
        return false;
    }
}
