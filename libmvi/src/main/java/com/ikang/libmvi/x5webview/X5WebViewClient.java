package com.ikang.libmvi.x5webview;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebView;

/**
 * @author IK-zhulk
 * @version 1.0.0
 * @describe {@link #}
 */
public class X5WebViewClient extends com.tencent.smtt.sdk.WebViewClient {

    private InterWebListener webListener;
    private X5WebView webView;

    /**
     * 设置监听时间，包括常见状态页面切换，进度条变化等
     *
     * @param listener listener
     */
    public void setWebListener(InterWebListener listener) {
        this.webListener = listener;
    }

    /**
     * 构造方法
     *
     * @param webView 需要传进来webview
     * @param context 上下文
     */
    public X5WebViewClient(X5WebView webView, Context context) {
        this.webView = webView;

    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.e("x5", "----url:" + url);
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        /*hitTestResult==null解决重定向问题*/
        WebView.HitTestResult hitTestResult = view.getHitTestResult();
        if (!TextUtils.isEmpty(url) && hitTestResult == null) {
            view.loadUrl(url);
            return true;
        }
        if (webListener != null) {
            return webListener.isOpenThirdApp(view, url);
        }
        return super.shouldOverrideUrlLoading(view, url);
    }


    @Override
    public void doUpdateVisitedHistory(WebView webView, String url, boolean isReload) {
        super.doUpdateVisitedHistory(webView, url, isReload);
        if (webListener != null) {
            webListener.doUpdateVisitedHistory(webView, url, isReload);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String
            failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        if (errorCode == 404) {
            //用javascript隐藏系统定义的404页面信息
            String data = "体检宝 Page NO FOUND！";
            view.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");
        } else {
            if (webListener != null) {
                webListener.showErrorView();
            }
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (webListener != null) {
            webListener.hindProgressBar();
            // html加载完成之后，可添加js函数
            webListener.onPageFinished(view, url);
        }
        super.onPageFinished(view, url);
        //页面finish后再发起图片加载
        if (!view.getSettings().getLoadsImagesAutomatically()) {
            view.getSettings().setLoadsImagesAutomatically(true);
        }
        //页面加载好以后，在放开图片
        view.getSettings().setBlockNetworkImage(false);

    }


    // SSL Error. Failed to validate the certificate chain,error: java.security.cert.CertPathValidatorExcept
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        //https忽略证书问题
        if (handler != null) {
            //表示等待证书响应
            handler.proceed();
        }
    }

    /**
     * 当缩放改变的时候会调用该方法
     *
     * @param view     view
     * @param oldScale 之前的缩放比例
     * @param newScale 现在缩放比例
     */
    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        super.onScaleChanged(view, oldScale, newScale);
        //视频全屏播放按返回页面被放大的问题
        if (newScale - oldScale > 7) {
            //异常放大，缩回去。
            view.setInitialScale((int) (oldScale / newScale * 100));
        }
    }

}
