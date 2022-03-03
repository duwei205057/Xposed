package com.test.hooktest;

import com.test.hooktest.utils.LogUtils;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Main implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    public static final String PREFS = "";
    //被HOOK的程序的包名和类名
    String sogouPackageName = "com.baidu.input";
    String baiduPackageName = "com.sohu.inputmethod.sogou";
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if(!loadPackageParam.packageName.equals(sogouPackageName) && !loadPackageParam.packageName.equals(baiduPackageName))
            return;
        LogUtils.log("Loaded app: " + loadPackageParam.packageName);

//        ICHook.initAllHooks(loadPackageParam);

//        HttpHook.initAllHooks(loadPackageParam);

//        OKHttpHook.initAllHooks(loadPackageParam);

//        CryptoHook.initAllHooks(loadPackageParam);

//        CloudHook.initAllHooks(loadPackageParam);

//        HashHook.initAllHooks(loadPackageParam);

//        ViewHook.initAllHooks(loadPackageParam);

//        ClassLoaderHook.initAllHooks(loadPackageParam);

        ApplicationHook.initAllHooks(loadPackageParam);

        InputMethodServiceHook.initAllHooks(loadPackageParam);

        ThreadHook.initAllHooks(loadPackageParam);

//        ChoreographerHook.initAllHooks(loadPackageParam);

//        ActivityHook.initAllHooks(loadPackageParam);
    }

    public static final String MY_PACKAGE_NAME = Main.class.getPackage().getName();
    public static XSharedPreferences sPrefs;

    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {
        sPrefs = new XSharedPreferences(MY_PACKAGE_NAME, PREFS);
        sPrefs.makeWorldReadable();
    }
}
