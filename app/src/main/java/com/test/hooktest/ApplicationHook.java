package com.test.hooktest;

import android.app.Application;
import android.content.Context;

import com.test.hooktest.utils.LogUtils;
import com.test.hooktest.utils.Util;

import dalvik.system.BaseDexClassLoader;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by @author dw
 * Date : 20-5-27
 */
public class ApplicationHook extends XC_MethodHook {

    public static final String TAG = "ApplicationHook      :";

    public static void initAllHooks(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws NoSuchFieldException {

        String applicationClass;
        if (loadPackageParam.packageName.contains("baidu")) {
            applicationClass = "com.baidu.input.NewImeApplication";
        } else {
            applicationClass = "com.sohu.inputmethod.sogou.SogouTinkerApplication";
        }

        try {
//        final Class<?> captureViewClass = findClass("com.sohu.inputmethod.sogou.feedback.TextCaptureView", loadPackageParam.classLoader);
            findAndHookMethod(applicationClass, loadPackageParam.classLoader, "attachBaseContext", Context.class, new XC_MethodHook() {
                long start;

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    start = System.nanoTime();
                    LogUtils.log(TAG + " Application.attach " + param.thisObject + " start ");
                }

                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    long cost = System.nanoTime() - start;
                    LogUtils.log(TAG + " Application.attach " + param.thisObject + " " + param.args[0] + " cost = "+cost);

                }
            });
            findAndHookMethod(applicationClass, loadPackageParam.classLoader, "onCreate", new XC_MethodHook() {
                long start;

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    start = System.nanoTime();
                }

                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    long cost = System.nanoTime() - start;
                    LogUtils.log(TAG + " Application.onCreate " + param.thisObject + " cost = "+cost);

                }
            });

            findAndHookConstructor(applicationClass, loadPackageParam.classLoader, new XC_MethodHook() {
                long start;

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    start = System.nanoTime();
                    Util.showBackTrace();
                }

                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    long cost = System.nanoTime() - start;
                    LogUtils.log(TAG + " Application.instance " + param.thisObject + " cost = "+cost);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
