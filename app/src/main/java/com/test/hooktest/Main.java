package com.test.hooktest;

import com.test.hooktest.utils.LogUtils;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Main implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    public static final String PREFS = "";
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

    public static final String MY_PACKAGE_NAME = Main.class.getPackage().getName();
    public static XSharedPreferences sPrefs;

    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {
        sPrefs = new XSharedPreferences(MY_PACKAGE_NAME, PREFS);
        sPrefs.makeWorldReadable();
    }
}
