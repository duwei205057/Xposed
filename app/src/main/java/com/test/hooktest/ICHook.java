package com.test.hooktest;

import android.os.Build;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
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
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedBridge.hookAllConstructors;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.getObjectField;

/**
 * Created by @author dw
 * Date : 20-4-15
 */
public class ICHook {

    public static final String TAG = "ICHook:";

    public static void initAllHooks(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        // replaceHookedMethod 替换方法
        // beforeHookedMethod 方法前执行
        // afterHookedMethod 方法后执行
        // 处理是的情况
        // 找到对应类的方法，进行hook，hook的方式有两种
        XposedHelpers.findAndHookMethod("com.android.internal.view.InputConnectionWrapper",     // 类名
                loadPackageParam.classLoader, // 类加载器
                "commitText", // 方法名
                CharSequence.class,   // 参数1
                int.class,   // 参数2
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        LogUtils.log("beforeHookedMethod commit " + param.args[0]+" " + param.args[1]);
                        param.args[0] = "";
//                        param.setResult(true);

                    }
                });
        XposedHelpers.findAndHookMethod("com.android.internal.view.InputConnectionWrapper",     // 类名
                loadPackageParam.classLoader, // 类加载器
                "getTextBeforeCursor", // 方法名
                int.class,   // 参数1
                int.class,   // 参数2
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        LogUtils.log("afterHookedMethod getTextBeforeCursor " + param.args[0]+" " + param.args[1] + " result=" + param.getResult());
                    }
                });
        XposedHelpers.findAndHookMethod("com.android.internal.view.InputConnectionWrapper",     // 类名
                loadPackageParam.classLoader, // 类加载器
                "getTextAfterCursor", // 方法名
                int.class,   // 参数1
                int.class,   // 参数2
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        LogUtils.log("afterHookedMethod getTextAfterCursor " + param.args[0]+" " + param.args[1] + " result=" + param.getResult());
                    }
                });
        XposedHelpers.findAndHookMethod("android.os.BaseBundle",     // 类名
                loadPackageParam.classLoader, // 类加载器
                "putString", // 方法名
                String.class,   // 参数1
                String.class,   // 参数2
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        LogUtils.log("afterHookedMethod Bundle.putString " + param.args[0]+" " + param.args[1]);
                    }
                });
    }
}
