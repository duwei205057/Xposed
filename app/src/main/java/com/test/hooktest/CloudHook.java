package com.test.hooktest;

import com.test.hooktest.utils.LogUtils;
import com.test.hooktest.utils.Util;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedBridge.hookAllConstructors;
import static de.robv.android.xposed.XposedHelpers.findClass;

/**
 * Created by @author dw
 * Date : 20-5-8
 */
public class CloudHook {

    public static final String TAG = "CloudHook:";

    public static void initAllHooks(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        try {
            XposedHelpers.findAndHookMethod("com.baidu.input.ime.cloudinput.CloudLog",     // 类名
                    loadPackageParam.classLoader, // 类加载器
                    "getEditorString", // 方法名
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            LogUtils.log(TAG + "CloudLog getEditorString   result=" + param.getResult());
                        }
                    });
        } catch (Error e) {
            LogUtils.log(e.toString());
        }
        try {
            XposedHelpers.findAndHookMethod("com.baidu.input.ime.cloudinput.CloudInfo",     // 类名
                    loadPackageParam.classLoader, // 类加载器
                    "get_app_name", // 方法名
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            LogUtils.log(TAG + "CloudInfo get_app_name   result=" + param.getResult());
                        }
                    });
        } catch (Error e) {
            LogUtils.log(e.toString());
        }
        try {
            XposedHelpers.findAndHookMethod("com.baidu.input.ime.cloudinput.manage.CloudDataManager",     // 类名
                    loadPackageParam.classLoader, // 类加载器
                    "getCloudRequsetData", // 方法名
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            LogUtils.log(TAG + "CloudDataManager getCloudRequsetData   result=" + new String((byte[]) param.getResult()));
                            Util.showBackTrace();
                        }
                    });
        } catch (Error e) {
            LogUtils.log(e.toString());
        }
        try {
            XposedHelpers.findAndHookMethod("com.baidu.input.ime.cloudinput.manage.CloudRequestData",     // 类名
                    loadPackageParam.classLoader, // 类加载器
                    "setReqContent", // 方法名
                    byte[].class,
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            LogUtils.log(TAG + "CloudRequestData setReqContent   byte=" + Util.toHexString((byte[]) param.args[0]));
                            Util.showBackTrace();
                        }
                    });
        } catch (Error e) {
            LogUtils.log(e.toString());
        }

        try {
            Class cardInfo = loadPackageParam.classLoader.loadClass("com.baidu.input.ime.cloudinput.CardInfo");
            XposedHelpers.findAndHookMethod("com.baidu.input.ime.cloudinput.CloudOutputService",     // 类名
                    loadPackageParam.classLoader, // 类加载器
                    "setCardInfo", // 方法名
                    cardInfo,
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            LogUtils.log(TAG + "CloudOutputService setCardInfo   cardInfo=" + param.args[0]);
                            Util.showBackTrace();
                        }
                    });
        } catch (Error e) {
            LogUtils.log(e.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            XposedHelpers.findAndHookMethod("java.net.DatagramSocket",     // 类名
                    loadPackageParam.classLoader, // 类加载器
                    "send", // 方法名
                    java.net.DatagramPacket.class,
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            LogUtils.log(TAG + "DatagramSocket send   =================");
                            Util.showBackTrace();
                        }
                    });
        } catch (Error e) {
            LogUtils.log(e.toString());
        }
    }


}
