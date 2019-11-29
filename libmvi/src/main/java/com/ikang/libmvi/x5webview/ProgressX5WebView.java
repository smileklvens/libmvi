/*
Copyright 2017 yangchong211（github.com/yangchong211）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.ikang.libmvi.x5webview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.ikang.libmvi.R;
import com.ikang.libmvi.base.BaseApp;
import com.tencent.smtt.sdk.WebView;

/**
 * @author IK-zhulk
 * @version 1.0.0
 * @describe {@link #}
 */
public class ProgressX5WebView extends FrameLayout {

    private X5WebView webView;
    private WebProgress mProgressBar;
    private InterWebListener mProxyListener;


    public ProgressX5WebView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ProgressX5WebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProgressX5WebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ProgressX5WebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    /**
     * @describe
     */
    public void setProxyListener(InterWebListener proxyListener) {
        this.mProxyListener = proxyListener;
    }


    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_progress_web_view, this, false);
        webView = view.findViewById(R.id.web_view);
        mProgressBar = view.findViewById(R.id.webProgress);
        addView(view);
        mProgressBar.setColor(
                ContextCompat.getColor(BaseApp.Companion.getInstance(), R.color.basic_color_ea5504),
                ContextCompat.getColor(BaseApp.Companion.getInstance(), R.color.basic_color_ea5504));
        mProgressBar.show();

        webView.getX5WebChromeClient().setWebListener(interWebListener);
        webView.getX5WebViewClient().setWebListener(interWebListener);
    }


    private InterWebListener interWebListener = new InterWebListener() {
        @Override
        public void hindProgressBar() {
            mProgressBar.hide();
        }


        @Override
        public void startProgress(int newProgress) {
            mProgressBar.setWebProgress(newProgress);
        }

        @Override
        public void showErrorView() {
            if (mProxyListener != null) {
                mProxyListener.showErrorView();
            }
        }


        @Override
        public void onReceivedTitle(WebView view, String title) {
            if (mProxyListener != null) {
                mProxyListener.onReceivedTitle(view, title);
            }
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            if (mProxyListener != null) {
                mProxyListener.doUpdateVisitedHistory(view, url, isReload);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (mProxyListener != null) {
                mProxyListener.onPageFinished(view, url);
            }
        }

        @Override
        public boolean isOpenThirdApp(WebView webView, String url) {
            if (mProxyListener != null) {
                return mProxyListener.isOpenThirdApp(webView,url);
            }
            return false;
        }
    };

    /**
     * 获取X5WebView对象
     *
     * @return 获取X5WebView对象
     */
    public X5WebView getWebView() {
        return webView;
    }

}
