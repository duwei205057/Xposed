package com.test.hooktest;

import android.os.Build;

import com.test.hooktest.utils.LogUtils;
import com.test.hooktest.utils.Util;

import java.net.URL;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedBridge.hookAllConstructors;
import static de.robv.android.xposed.XposedHelpers.findClass;

/**
 * Created by @author dw
 * Date : 20-5-8
 */
public class OKHttpHook {

    public static final String TAG = "OKHttpHook:";

    public static void initAllHooks(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        try {
            XposedHelpers.findAndHookMethod("okhttp3.Request$Builder",     // 类名
                    loadPackageParam.classLoader, // 类加载器
                    "url", // 方法名
                    String.class,   // 参数1
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            LogUtils.log(TAG + "okhttp3.Request$Builder url " + param.args[0] + " result=" + param.getResult());
                        }
                    });
        } catch (Error e) {
            LogUtils.log(e.toString());
        }
        try {
            final Class<?> httpUrl = findClass("okhttp3.HttpUrl", loadPackageParam.classLoader);
            hookAllConstructors(httpUrl, new XC_MethodHook() {

                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    String p = "";
                    if (param.args.length > 0) {
                        p = param.args[0] + "";
                    }
                    LogUtils.log(TAG + ": HttpUrl Constructor " + p + "");
                }
            });
        } catch (Error e) {
            LogUtils.log(e.toString());
        }
        try {
            XposedHelpers.findAndHookMethod("java.net.PlainSocketImpl$PlainSocketOutputStream",     // 类名
                    loadPackageParam.classLoader, // 类加载器
                    "write", // 方法名
                    byte[].class,   // 参数1
                    int.class,   // 参数1
                    int.class,   // 参数1
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            LogUtils.log(TAG + "PlainSocketOutputStream  write !" + new String((byte[]) param.args[0]));
                        }
                    });
        } catch (Error e) {
            LogUtils.log(e.toString());
        }
        try {
            XposedHelpers.findAndHookMethod("com.android.org.conscrypt.OpenSSLSocketImpl$SSLOutputStream",     // 类名
                    loadPackageParam.classLoader, // 类加载器
                    "write", // 方法名
                    byte[].class,   // 参数1
                    int.class,   // 参数1
                    int.class,   // 参数1
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            LogUtils.log(TAG + "OpenSSLSocketImpl  write !" + new String((byte[]) param.args[0]));
                        }
                    });
        } catch (Error e) {
            LogUtils.log(e.toString());
        }

        try {
            final Class<?> httpUrl = findClass("java.lang.Thread", loadPackageParam.classLoader);
            hookAllConstructors(httpUrl, new XC_MethodHook() {

                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    String p = "";
                    if (param.args.length > 0) {
                        p = param.args[0] + "";
                    }
                    LogUtils.log(TAG + ": Thread Constructor " + p + "");
                }
            });
        } catch (Error e) {
            LogUtils.log(e.toString());
        }
    }


}
