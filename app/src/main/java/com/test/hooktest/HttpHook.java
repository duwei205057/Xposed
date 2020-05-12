package com.test.hooktest;

import android.os.Build;

import com.test.hooktest.utils.LogUtils;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedBridge.hookAllConstructors;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.getObjectField;

public class HttpHook {

    public static final String TAG = "HttpHook:";

    public static void initAllHooks(final XC_LoadPackage.LoadPackageParam loadPackageParam) {

        try {
            final Class<?> httpUrlConnection = findClass("java.net.HttpURLConnection", loadPackageParam.classLoader);
            hookAllConstructors(httpUrlConnection, new XC_MethodHook() {

                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    if (param.args.length != 1 || param.args[0].getClass() != URL.class) {
                        return;
                    }

                    LogUtils.log(TAG + "HttpURLConnection: Constructor " + param.args[0] + "");
                }
            });
        } catch (Error e) {
            LogUtils.log(e.toString());
        }

        XC_MethodHook RequestHook = new XC_MethodHook() {

            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                HttpURLConnection urlConn = (HttpURLConnection) param.thisObject;

                if (urlConn != null) {
                    StringBuilder sb = new StringBuilder();
                    boolean connected = (boolean)getObjectField(param.thisObject, "connected");

                    LogUtils.log("connected=="+connected);
                    if(!connected){


                        Map<String, List<String>> properties = urlConn.getRequestProperties();
                        if (properties != null && properties.size() > 0) {


                            for (Map.Entry<String, List<String>> entry : properties.entrySet()){
                                sb.append(entry.getKey()+": "+entry.getValue()+", ");
                            }
                        }

                        DataOutputStream dos = (DataOutputStream) param.getResult();


                        LogUtils.log(TAG + "REQUEST: method=" + urlConn.getRequestMethod() + " " +
                                "URL=" + urlConn.getURL().toString() + " " +
                                "Params=" + sb.toString());
                    }
                }

            }
        };


        XC_MethodHook ResponseHook = new XC_MethodHook() {

            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                HttpURLConnection urlConn = (HttpURLConnection) param.thisObject;

                if (urlConn != null) {
                    StringBuilder sb = new StringBuilder();
                    int code = urlConn.getResponseCode();
                    if(code==200){

                        Map<String, List<String>> properties = urlConn.getHeaderFields();
                        if (properties != null && properties.size() > 0) {

                            for (Map.Entry<String, List<String>> entry : properties.entrySet()) {
                                sb.append(entry.getKey() + ": " + entry.getValue() + ", ");
                            }
                        }
                    }

                    LogUtils.log(TAG + "RESPONSE: method=" + urlConn.getRequestMethod() + " " +
                            "URL=" + urlConn.getURL().toString() + " " +
                            "Params=" + sb.toString());
                }

            }
        };

        /*try {
            final Class<?> okHttpClient = findClass("com.android.okhttp.OkHttpClient", loadPackageParam.classLoader);
            if(okHttpClient != null) {
                findAndHookMethod(okHttpClient, "open", URI.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        URI uri = null;
                        if (param.args[0] != null)
                            uri = (URI) param.args[0];
                        LogUtils.log(TAG + "OkHttpClient: " + uri.toString() + "");
                    }
                });
            }
        } catch (Error e) {
            LogUtils.log(e.toString());
        }*/


        try {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                findAndHookMethod("libcore.net.http.HttpURLConnectionImpl", loadPackageParam.classLoader, "getOutputStream", RequestHook);
            } else {
                //com.squareup.okhttp.internal.http.HttpURLConnectionImpl
                final Class<?> httpURLConnectionImpl = findClass("com.android.okhttp.internal.huc.HttpURLConnectionImpl", loadPackageParam.classLoader);
                if(httpURLConnectionImpl != null) {
                    findAndHookMethod(httpURLConnectionImpl, "getOutputStream", RequestHook);
                    findAndHookMethod(httpURLConnectionImpl, "getInputStream", ResponseHook);
                }
            }
        } catch (Error e) {
            LogUtils.log(e.toString());
        }

        findAndHookMethod(SSLContext.class, "init",
                KeyManager[].class, TrustManager[].class, SecureRandom.class, new XC_MethodHook() {

                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        KeyManager[] km = (KeyManager[]) param.args[0];
                        TrustManager[] tm_ = (TrustManager[]) param.args[1];

                        if (tm_ != null && tm_[0] != null) {
                            X509TrustManager tm = (X509TrustManager) tm_[0];
                            X509Certificate[] chain = new X509Certificate[]{};

                            LogUtils.log(TAG + "Possible pinning.");
                            boolean check = false;
                            /**
                             try {
                             tm.checkClientTrusted(chain, "");
                             tm.checkServerTrusted(chain, "");

                             }catch (CertificateException ex){
                             check = true;
                             }

                             if(check){
                             LogUtils.log(TAG + " Custom TrustManager - Possible pinning.");
                             }else {
                             LogUtils.log(TAG + " App not verify SSL.");
                             }**/
                        }
                    }
                });
    }
}
