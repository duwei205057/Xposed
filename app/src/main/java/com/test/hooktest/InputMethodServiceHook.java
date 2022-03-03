package com.test.hooktest;

import com.test.hooktest.utils.LogUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import dalvik.system.BaseDexClassLoader;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedBridge.hookMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

/**
 * Created by @author dw
 * Date : 20-5-27
 */
public class InputMethodServiceHook extends XC_MethodHook {

    public static final String TAG = "InputMethodServiceHook      :";

    public static void initAllHooks(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws NoSuchFieldException {

        String serviceClassName;
        if (loadPackageParam.packageName.contains("baidu")) {
            serviceClassName = "com.baidu.input.ImeService";
        } else {
            serviceClassName = "com.sohu.inputmethod.sogou.SogouIME";
        }
        try {
            Class serviceClass = findClass(serviceClassName, loadPackageParam.classLoader);

            Method[] m = serviceClass.getDeclaredMethods();
            for (Method method : m) {
                if (!Modifier.isAbstract(method.getModifiers()) && !Modifier.isNative(method.getModifiers())
                        && !Modifier.isInterface(method.getModifiers())) {
                    hookTargetMethod(method);
                }
            }

            findAndHookConstructor(serviceClass, new XC_MethodHook() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void hookTargetMethod(final Method method) {
        String methodName = method.getName();
        if (!methodName.startsWith("on") && !"showWindow".equals(methodName)) {
            return;
        }
        hookMethod(method, new XC_MethodHook() {
            long start;

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                start = System.nanoTime();
            }

            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                long cost = System.nanoTime() - start;
                LogUtils.log(TAG + method.getName() + " " + param.thisObject + " cost = "+cost);

            }
        });
    }
}
