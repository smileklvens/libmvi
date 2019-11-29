package com.ikang.libmvi.x5webview

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.databinding.ViewDataBinding
import com.ikang.libmvi.R
import com.ikang.libmvi.base.NoViewModel
import com.ikang.libmvi.base.ui.activity.BaseActivity
import com.tencent.smtt.sdk.WebView
import kotlinx.android.synthetic.main.activity_webview_tbs.*
import kotlinx.android.synthetic.main.layout_simple_toolbar.view.*


/**
 * @author ikang-zhulk
 * @version 1.0.0
 * @describe {@link #}
 */

class X5WebViewActivity : BaseActivity<NoViewModel, ViewDataBinding>() {
    var mForceShowTitle = false
    var mShowShare = false
    lateinit var mUrl: String
    var mTitle: String? = ""
    var mImage: String? = null
    var mFrom = 0
    var historyUrl = ""
    override fun layoutId() = R.layout.activity_webview_tbs


    override fun initView(savedInstanceState: Bundle?) {
        window.setFormat(PixelFormat.TRANSLUCENT)
        getIntentData()
        initprogressWebView()
        setTitle()
    }

    /**
     * 使用singleTask启动模式的Activity在系统中只会存在一个实例。
     * 如果这个实例已经存在，intent就会通过onNewIntent传递到这个Activity。
     * 否则新的Activity实例被创建。
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        getIntentData()
    }

    override fun onPause() {
        super.onPause()
        progressWebView.webView.onPause()
    }

    override fun onResume() {
        super.onResume()
        // 支付宝网页版在打开文章详情之后,无法点击按钮下一步
        progressWebView?.webView?.run {
            onResume()
            resumeTimers()
        }
    }

    override fun onDestroy() {
        progressWebView.webView?.run {
            val parent = parent as ViewGroup
            parent.removeView(this)
            removeAllViews()
            loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            stopLoading()
            webChromeClient = null
            webViewClient = null
            destroy()
        }
        super.onDestroy()
    }

    override fun initData() {
        progressWebView.getWebView().loadUrl(mUrl)
        progressWebView.setProxyListener(object : DefaultInterWebListener() {
            override fun onReceivedTitle(
                view: WebView?,
                title: String
            ) {
                super.onReceivedTitle(view, title)
                if (TextUtils.isEmpty(mTitle)) {
                    mTitle = title
                    mToolbar?.setMainTitle(mTitle)
                }
            }

            override fun doUpdateVisitedHistory(
                view: WebView?,
                url: String,
                isReload: Boolean
            ) {
                super.doUpdateVisitedHistory(view, url, isReload)
                historyUrl = url
            }


            override fun onPageFinished(
                view: WebView?,
                url: String?
            ) {
                super.onPageFinished(view, url)

            }

            override fun isOpenThirdApp(
                view: WebView?,
                url: String?
            ): Boolean {
                return interOpenThirdApp(view, url)
            }
        })

        addJSInterface()
    }


    /**
     * @describe 暴露给子类，让子类灵活处理
     */
    fun interOpenThirdApp(
        view: WebView?,
        url: String?
    ): Boolean {
        return false
    }

    /**
     * @describe
     */
    fun addJSInterface() { // 与js交互
        progressWebView.webView.addJavascriptInterface(IKCommonJSInterface(this), "ikang")
    }

    fun getIntentData() {
        intent?.run {
            mForceShowTitle = getBooleanExtra(KEY_WEB_FORCE_SHOW_TITLE, true)
            mShowShare = getBooleanExtra(KEY_WEB_SHOW_SHARE, false)
            mUrl = getStringExtra(KEY_WEB_URL)
            mTitle = getStringExtra(KEY_WEB_TITLE)
            mImage = getStringExtra(KEY_WEB_IMG)
            mFrom = getIntExtra(KEY_WEB_FROM, -1)
        }

        if (TextUtils.isEmpty(mUrl)) {
            toast("请传入地址")
            supportFinishAfterTransition()
        }
    }

    @SuppressLint("SetJavaScriptEnabled", "AddJavascriptInterface")
    fun initprogressWebView() {
        //copy 体检宝
//        WebX5Util.synchronousWebCookies(this, mUrl)
//
//        val webView = progressWebView.webView
//        val ws = webView.settings
//        webView.removeJavascriptInterface("searchBoxJavaBridge_")
//        webView.removeJavascriptInterface("accessibilityTraversal")
//        webView.removeJavascriptInterface("accessibility")
//        ws.savePassword = false
//        ws.javaScriptCanOpenWindowsAutomatically = true
//        val ua = ws.userAgentString
//        ws.userAgentString = ua + BasicConfigConst.APP_TYPE
//        ws.allowFileAccess = true
//        if (ConfigConst.ISUSE_APM) {
//            NBSWebChromeX5Client.addWebViewBridge(webView) //调用addWebViewBridge方法，将X5实例传入
//        }
    }

    /**
     * @describe 控制标题
     */
    fun setTitle() {
        if (mForceShowTitle) {
            mToolbar?.run {
                visibility = View.VISIBLE
                setMainTitle(mTitle)
                setRightTitleDrawable(R.drawable.icon_shared)
                txt_right_title.visibility = if (mShowShare) View.VISIBLE else View.GONE
                setLeftTitleClickListener(object : OnClickListener {
                    override fun onClick(v: View?) {
                        dispatchBackEvent()
                    }
                })
            }
        } else {
            mToolbar?.setVisibility(View.GONE)
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            dispatchBackEvent()
        } else super.onKeyDown(keyCode, event)
    }

    fun dispatchBackEvent(): Boolean {
        supportFinishAfterTransition()
        return true
    }

    companion object {
        const val KEY_WEB_FORCE_SHOW_TITLE = "web_force_show_title"
        const val KEY_WEB_SHOW_SHARE = "web_show_share"
        const val KEY_WEB_TITLE = "web_title"
        const val KEY_WEB_IMG = "web_img"
        const val KEY_WEB_URL = "web_url"
        const val KEY_WEB_FROM = "web_from"

        //仅仅finish当前页面
        const val FROM_FORE_FINISH = 1
        //从我的页面
        const val FROM_ACCOUNT_SHARE = 10001
        //从我的页面
        const val FROM_ACCOUNT_INSURANCE = 10002
        //从支付页面
        const val FROM_PANMENT_INSURANCE = 10004
        //从支付成功页面
        const val FROM_APPOINTI_INSURANCE = 10008
        //从首页报告详情页面
        const val FROM_HOME_REPORTS = 10016
        //从首页秒杀活动
        const val FROM_HOME_SPIKEACTIVITY = 10032
        //从广告页进来
        const val FROM_ADVERT = 10064

    }

    @IntDef(
        FROM_FORE_FINISH,
        FROM_ACCOUNT_SHARE,
        FROM_ACCOUNT_INSURANCE,
        FROM_PANMENT_INSURANCE,
        FROM_APPOINTI_INSURANCE,
        FROM_HOME_REPORTS,
        FROM_HOME_SPIKEACTIVITY,
        FROM_ADVERT
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class FROM


}