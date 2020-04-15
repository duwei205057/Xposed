package com.test.hooktest;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.inputmethod.InputConnection;

import java.net.URL;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedBridge.hookAllConstructors;
import static de.robv.android.xposed.XposedHelpers.findClass;

public class Main implements IXposedHookLoadPackage {

    //被HOOK的程序的包名和类名
    String packName1 = "com.baidu.input";
    String packName2 = "com.sohu.inputmethod.sogou";
    String className = "android.view.inputmethod.InputConnectionWrapper";
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if(!loadPackageParam.packageName.equals(packName1) && !loadPackageParam.packageName.equals(packName2))
            return;
        LogUtils.log("Loaded app: " + loadPackageParam.packageName);

        ICHook.initAllHooks(loadPackageParam);

        HttpHook.initAllHooks(loadPackageParam);
    }
}
