package com.test.hooktest;

import android.app.Activity;
import android.app.ActivityThread;
import android.app.Instrumentation;
import android.app.LoadedApk;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.test.hooktest.utils.LogUtils;
import com.test.hooktest.utils.Util;

import java.util.Arrays;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedBridge.hookAllMethods;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

/**
 * Created by acpm on 14/01/16.
 */
public class ActivityHook extends XC_MethodHook {

    public static final String TAG = "ActivityHook:";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void initAllHooks(final XC_LoadPackage.LoadPackageParam loadPackageParam) {



        try {
            findAndHookMethod(Activity.class, "startActivityForResult", Intent.class, int.class, Bundle.class, new XC_MethodHook() {

                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    LogUtils.log(TAG + param.thisObject + " startActivityForResult  intent = " + param.args[0]);
                    Util.showBackTrace();
                }
            });

            final Class<?> classContextImpl = findClass("android.app.ContextImpl", loadPackageParam.classLoader);
            findAndHookMethod(classContextImpl, "startActivity", Intent.class, Bundle.class, new XC_MethodHook() {

                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    LogUtils.log(TAG + param.thisObject + " startActivity intent = " + param.args[0]);
                    Util.showBackTrace();
                }
            });

            findAndHookMethod(classContextImpl, "startActivityAsUser", Intent.class, Bundle.class, UserHandle.class ,new XC_MethodHook() {

                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    LogUtils.log(TAG + param.thisObject + " startActivityAsUser intent = " + param.args[0]);
                    Util.showBackTrace();
                }
            });

            final Class<?> classReceiverData = findClass("android.app.ActivityThread.ReceiverData", loadPackageParam.classLoader);
            findAndHookMethod(ActivityThread.class, "handleReceiver", classReceiverData ,new XC_MethodHook() {

                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    LogUtils.log(TAG + param.thisObject + " handleReceiver " + param.args[0]);
                    Util.showBackTrace();
                }
            });

            final Class<?> classCreateServiceData = findClass("android.app.ActivityThread.CreateServiceData", loadPackageParam.classLoader);
            findAndHookMethod(ActivityThread.class, "handleCreateService", classCreateServiceData ,new XC_MethodHook() {

                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    LogUtils.log(TAG + param.thisObject + " handleCreateService " + param.args[0]);
                    Util.showBackTrace();
                }
            });
//            findAndHookMethod(ActivityThread.class, "attach", boolean.class, long.class ,new XC_MethodHook() {
//
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    LogUtils.log(TAG + param.thisObject + " attach " + param.args[0] + " " + param.args[1]);
//                    Util.showBackTrace();
//                }
//            });
//
//            final Class<?> classAppBindData = findClass("android.app.ActivityThread.AppBindData", loadPackageParam.classLoader);
//            findAndHookMethod(ActivityThread.class, "handleBindApplication", classAppBindData ,new XC_MethodHook() {
//
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    LogUtils.log(TAG + param.thisObject + " handleBindApplication " + param.args[0]);
//                    Util.showBackTrace();
//                }
//            });
//
//            final Class<?> classActivityClientRecord = findClass("android.app.ActivityThread.ActivityClientRecord", loadPackageParam.classLoader);
//            findAndHookMethod(ActivityThread.class, "performLaunchActivity", classActivityClientRecord, Intent.class ,new XC_MethodHook() {
//
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    LogUtils.log(TAG + param.thisObject + " performLaunchActivity " + param.args[0] + "  " + param.args[1]);
//                    Util.showBackTrace();
//                }
//            });
//            final Class<?> classPendingTransactionActions = findClass("android.app.servertransaction.PendingTransactionActions", loadPackageParam.classLoader);
//            findAndHookMethod(ActivityThread.class, "handleLaunchActivity", classActivityClientRecord, classPendingTransactionActions, Intent.class ,new XC_MethodHook() {
//
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    LogUtils.log(TAG + param.thisObject + " handleLaunchActivity " + param.args[0] + "  " + param.args[1] + "  " + param.args[2]);
//                    Util.showBackTrace();
//                }
//            });

            hookAllMethods(ActivityThread.class, "handleLaunchActivity" , new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    LogUtils.log(TAG + param.thisObject + " handleLaunchActivity " + Arrays.toString(param.args));
                    Util.showBackTrace();
                }
            });
            hookAllMethods(ActivityThread.class, "handleBindApplication" , new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    LogUtils.log(TAG + param.thisObject + " handleBindApplication " + Arrays.toString(param.args));
                    Util.showBackTrace();
                }
            });
            hookAllMethods(ActivityThread.class, "attach" , new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    LogUtils.log(TAG + param.thisObject + " attach " + Arrays.toString(param.args));
                    Util.showBackTrace();
                }
            });

//            final Class<?> classB = findClass("com.android.server.am.ActivityManagerService", loadPackageParam.classLoader);
//            final Class<?> classC = findClass("android.app.IApplicationThread", loadPackageParam.classLoader);
//            findAndHookMethod(classB, "attachApplicationLocked", classC, int.class, int.class, long.class ,new XC_MethodHook() {
//
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    LogUtils.log(TAG + param.thisObject + " attachApplicationLocked ");
//                    Util.showBackTrace();
//                }
//            });
        }catch (Error e){
            XposedBridge.log("ERROR_PROCESS: "+e.getMessage());
        }
    }
}
