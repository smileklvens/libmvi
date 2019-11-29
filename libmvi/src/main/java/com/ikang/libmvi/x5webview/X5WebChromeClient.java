package com.ikang.libmvi.x5webview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;


import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebView;

import static android.app.Activity.RESULT_OK;


/**
 * @author IK-zhulk
 * @version 1.0.0
 * @describe {@link #}
 */
public class X5WebChromeClient extends com.tencent.smtt.sdk.WebChromeClient {
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadMessageForAndroid5;
    /**
     * 注意h5中调用上传图片，resultCode保持一致性
     */
    public static int FILE_CHOOSER_RESULT_CODE = 1;
    public static int FILE_CHOOSER_RESULT_CODE_5 = 2;

    private Activity activity;
    private X5WebView webView;
    private InterWebListener webListener;




    /**
     * 构造方法
     * @param activity                          上下文
     */
    public X5WebChromeClient(X5WebView webView , Activity activity) {
        this.webView = webView;
        this.activity = activity;
    }

    /**
     * 设置监听时间，包括常见状态页面切换，进度条变化等
     *
     * @param listener listener
     */
    public void setWebListener(InterWebListener listener) {
        this.webListener = listener;
    }



    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (webListener != null) {
            webListener.startProgress(newProgress);
            int max = 85;
            if (newProgress > max) {
                webListener.hindProgressBar();
            }
        }
    }


    @Override
    public boolean onJsAlert(WebView webView, String url, String message, JsResult jsResult) {
        jsResult.confirm();
        return true;
    }


    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        if (title.contains("404") || title.contains("网页无法打开")) {
            if (webListener != null) {
                webListener.showErrorView();
            }
        } else {
            // 设置title
            if (webListener != null) {
                webListener.onReceivedTitle(view, title);
            }
        }
    }

    /**
     * 打开文件夹
     * @param uploadMsg                         msg
     * @param acceptType                        type
     * @param capture                           capture
     */
    @Override
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        openFileChooserImpl(uploadMsg);
    }

    /**
     * For Android > 5.0
     * @param webView                           webview
     * @param uploadMsg                         msg
     * @param fileChooserParams                 参数
     * @return
     */
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg,
                                     FileChooserParams fileChooserParams) {
        openFileChooserImplForAndroid5(uploadMsg);
        return true;
    }


    /**
     * 打开文件夹
     * @param uploadMsg                         msg
     */
    private void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
        if (activity!=null){
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            activity.startActivityForResult(
                    Intent.createChooser(i, "文件选择"), FILE_CHOOSER_RESULT_CODE);
        }
    }

    /**
     * 打开文件夹，Android5.0以上
     * @param uploadMsg                         msg
     */
    private void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg) {
        if (activity!=null){
            mUploadMessageForAndroid5 = uploadMsg;
            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            contentSelectionIntent.setType("image/*");
            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "图片选择");
            activity.startActivityForResult(chooserIntent, FILE_CHOOSER_RESULT_CODE_5);
        }
    }

    /**
     * 5.0以下 上传图片成功后的回调
     */
    public void uploadMessage(Intent intent, int resultCode) {
        if (null == mUploadMessage) {
            return;
        }
        Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
        mUploadMessage.onReceiveValue(result);
        mUploadMessage = null;
    }

    /**
     * 5.0以上 上传图片成功后的回调
     */
    public void uploadMessageForAndroid5(Intent intent, int resultCode) {
        if (null == mUploadMessageForAndroid5) {
            return;
        }
        Uri result = (intent == null || resultCode != RESULT_OK) ? null : intent.getData();
        if (result != null) {
            mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
        } else {
            mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
        }
        mUploadMessageForAndroid5 = null;
    }
}
