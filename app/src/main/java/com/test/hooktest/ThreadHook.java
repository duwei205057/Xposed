package com.test.hooktest;

import com.test.hooktest.utils.LogUtils;

import dalvik.system.BaseDexClassLoader;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by @author dw
 * Date : 20-5-27
 */
public class ThreadHook extends XC_MethodHook {

    public static final String TAG = "ClassLoaderHook      :";

    public static void initAllHooks(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws NoSuchFieldException {

        findAndHookConstructor(Thread.class, new XC_MethodHook() {
            long start;

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                start = System.nanoTime();
            }

            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                long cost = System.nanoTime() - start;
                LogUtils.log(TAG + " Service.instance " + param.thisObject + " cost = "+cost);

            }
        });
    }
}
