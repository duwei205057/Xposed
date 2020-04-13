package com.test.hooktest;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.inputmethod.InputConnection;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Main implements IXposedHookLoadPackage {

    //被HOOK的程序的包名和类名
//    String packName = "com.test.xposeddemo";
//    String className = "com.test.xposeddemo.MainActivity";
    String packName1 = "com.baidu.input";
    String packName2 = "com.sohu.inputmethod.sogou";
    String className = "android.view.inputmethod.InputConnectionWrapper";
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if(!loadPackageParam.packageName.equals(packName1) && !loadPackageParam.packageName.equals(packName2))
            return;
        XposedBridge.log("Loaded app: " + loadPackageParam.packageName);

        // 找到对应的方法，进行替换
        // 参数 1：类名
        // 参数 2: 方法名
        // 参数 3：实现监听，重写方法
        // replaceHookedMethod 替换方法
        // beforeHookedMethod 方法前执行
        // afterHookedMethod 方法后执行
        XposedHelpers.findAndHookMethod(TelephonyManager.class, "getSubscriberId", new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                return "Hook 成功了 哈哈！！！";
            }
        });

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
//                        XposedBridge.log("Loaded app: ----------commitText------------");
                        Log.d("yy", "beforeHookedMethod commit " + param.args[0]+" " + param.args[1]);
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
                        Log.d("yy", "afterHookedMethod getTextBeforeCursor " + param.args[0]+" " + param.args[1] + " result=" + param.getResult());
                    }
                });
    }
}
