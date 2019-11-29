package com.ikang.libmvi.x5webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.ikang.libmvi.R;
import com.ikang.libmvi.base.BaseApp;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author IK-zhulk
 * @version 1.0.0
 * @describe {@link #}
 */
public class WebX5Util {

    static final String WEB_CACHE_PATCH = "/web_cache";
    static final String DOWNLOAD_PATH = "web_download";

    private static boolean isInit = false;


    /**
     * 为H5页面同步用户的cookie和session
     *
     * @param context 上下文 用来初始化
     * @param url     注入信息的url
     */
    public static void synchronousWebCookies(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(context, R.string.url_null_tip, Toast.LENGTH_SHORT).show();
        } else {
            initCookiesManager();

            removeAllCookies();

            HashMap<String, String> map = new HashMap<>(4);
//            map.put(url, "access_token=" + AccountManager.getAccount(context).access_token);
//            map.put(url, "APP_SERVICE_SESSION=" + AccountManager.getAccount(context).pmed_sessionid);
            map.put(url, "platform=android");
            syncCookie(map);

            String newCookie = getCookiesByUrl(url);

        }
    }


    private static synchronized void initCookiesManager() {
        if (!isInit) {
            createCookiesSyncInstance();
            isInit = true;
        }
    }

    private static void createCookiesSyncInstance() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(BaseApp.Companion.getInstance());
        }
    }

    //获取Cookie
    public static String getCookiesByUrl(String url) {
        return CookieManager.getInstance() == null ? null : CookieManager.getInstance().getCookie(url);
    }

    public static void syncCookie(String url, String cookies) {
        CookieManager mCookieManager = CookieManager.getInstance();
        if (mCookieManager != null) {
            mCookieManager.setAcceptCookie(true);
            mCookieManager.setCookie(url, cookies);
            toSyncCookies();
        }
    }

    public static void syncCookie(HashMap<String, String> cookies) {
        CookieManager mCookieManager = CookieManager.getInstance();
        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            if (mCookieManager != null) {
                mCookieManager.setCookie(entry.getKey(), entry.getValue());
            }
        }
        toSyncCookies();
    }


    public static void removeExpiredCookies() {
        CookieManager mCookieManager = null;
        if ((mCookieManager = CookieManager.getInstance()) != null) { //同步清除{
            mCookieManager.removeExpiredCookie();
            toSyncCookies();
        }
    }

    public static void removeAllCookies() {
        removeAllCookies(null);

    }


    //Android  4.4  NoSuchMethodError: android.webkit.CookieManager.removeAllCookies
    public static void removeAllCookies(@Nullable ValueCallback<Boolean> callback) {

        if (callback == null)
            callback = getDefaultIgnoreCallback();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookie();
            toSyncCookies();
            callback.onReceiveValue(!CookieManager.getInstance().hasCookies());
            return;
        }
        CookieManager.getInstance().removeAllCookies(callback);
        toSyncCookies();
    }


    private static ValueCallback<Boolean> getDefaultIgnoreCallback() {

        return new ValueCallback<Boolean>() {
            @Override
            public void onReceiveValue(Boolean ignore) {
                Log.e("Info", "removeExpiredCookies:" + ignore);
            }
        };
    }


    private static void toSyncCookies() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().sync();
            return;
        }
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                CookieManager.getInstance().flush();

            }
        });
    }


    public static String getCachePath() {
        return BaseApp.Companion.getInstance().getCacheDir().getAbsolutePath() + WEB_CACHE_PATCH;
    }

    public static String getDatabasesCachePath() {
        return BaseApp.Companion.getInstance().getDir("database", Context.MODE_PRIVATE).getPath();
    }


    public static void openX5Act(Context context, String url, @X5WebViewActivity.FROM int from) {
        openX5Act(context, url, "", "", from, true, false);
    }

    public static void openX5Act(Context context, String url, String title, @X5WebViewActivity.FROM int from) {
        openX5Act(context, url, title, "", from, true, false);
    }


    public static void openX5Act(Context context, String url, String title, String image, @X5WebViewActivity.FROM int from) {
        openX5Act(context, url, title, image, from, true, false);
    }

    /**
     * @describe
     */
    public static void openX5Act(Context context, String url, String title, String image, @X5WebViewActivity.FROM int from, boolean forceShowTitle, boolean showShare) {
        openX5Act(X5WebViewActivity.class, context, url, title, image, from, forceShowTitle, showShare);
    }


    /**
     * @describe
     */
    public static void openX5Act(Class<?> pClass, Context context, String url, String title, String image, @X5WebViewActivity.FROM int from, boolean forceShowTitle, boolean showShare) {
        if (context == null) {
            Log.e("openX5Act", "openX5Act:context can not be null");
            return;
        }
        if (TextUtils.isEmpty(url)) {
            Log.e("openX5Act", "openX5Act:url can not be null");
            return;
        }


        Bundle bundle = new Bundle();
        bundle.putString(X5WebViewActivity.KEY_WEB_URL, url);
        bundle.putInt(X5WebViewActivity.KEY_WEB_FROM, from);
        if (!TextUtils.isEmpty(title)) {
            bundle.putString(X5WebViewActivity.KEY_WEB_TITLE, title);
        }
        if (!TextUtils.isEmpty(image)) {
            bundle.putString(X5WebViewActivity.KEY_WEB_IMG, image);
        }
        bundle.putBoolean(X5WebViewActivity.KEY_WEB_FORCE_SHOW_TITLE, forceShowTitle);
        bundle.putBoolean(X5WebViewActivity.KEY_WEB_SHOW_SHARE, showShare);


        Intent intent = new Intent(context, pClass);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }


    /**
     * 处理三方链接
     * 网页里可能唤起其他的app
     */
    public static boolean handleThirdApp(Activity activity, WebView webView, String backUrl) {
        /*http开头直接跳过*/
        if (backUrl.startsWith("http")) {
            // 可能有提示下载Apk文件
            if (backUrl.contains(".apk")) {
                startActivity(activity, backUrl);
                return true;
            }
            return false;
        }

        boolean isJump = false;
        /*允许以下应用唤起App，可根据需求 添加或取消*/
        if (
//                backUrl.startsWith("tbopen:")// 淘宝
//                        || backUrl.startsWith("openapp.jdmobile:")// 京东
//                        || backUrl.startsWith("jdmobile:")//京东
                backUrl.startsWith("weixin:")// 微信
                        || backUrl.startsWith("alipay:")// 支付宝
                        || backUrl.startsWith("mailto:")// 邮件
                        || backUrl.startsWith("tel:")// 电话

//                        || backUrl.startsWith("alipays:")//支付宝
//                        || backUrl.startsWith("zhihu:")// 知乎
//                        || backUrl.startsWith("vipshop:")//
//                        || backUrl.startsWith("youku:")//优酷
//                        || backUrl.startsWith("uclink:")// UC
//                        || backUrl.startsWith("ucbrowser:")// UC
//                        || backUrl.startsWith("newsapp:")//
//                        || backUrl.startsWith("sinaweibo:")// 新浪微博
//                        || backUrl.startsWith("suning:")//
//                        || backUrl.startsWith("pinduoduo:")// 拼多多
//                        || backUrl.startsWith("baiduboxapp:")// 百度
//                        || backUrl.startsWith("qtt:")//
        ) {
            isJump = true;
        }
        if (isJump) {
            startActivity(activity, backUrl);
        }
        return isJump;
    }


    private static void startActivity(Context context, String url) {
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri uri = Uri.parse(url);
            intent.setData(uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
