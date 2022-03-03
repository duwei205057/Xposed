package com.test.hooktest;

import android.inputmethodservice.InputMethodService;
import android.view.View;

import com.test.hooktest.utils.LogUtils;
import com.test.hooktest.utils.Util;

import java.util.Arrays;

import dalvik.system.BaseDexClassLoader;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by @author dw
 * Date : 20-5-27
 */
public class ClassLoaderHook extends XC_MethodHook {

    public static final String TAG = "ClassLoaderHook      :";

    public static void initAllHooks(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws NoSuchFieldException {

//        final Class<?> captureViewClass = findClass("com.sohu.inputmethod.sogou.feedback.TextCaptureView", loadPackageParam.classLoader);
        findAndHookMethod(BaseDexClassLoader.class, "findClass", String.class, new XC_MethodHook() {

            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                LogUtils.log(TAG + " BaseDexClassLoader.findClass " + param.thisObject + " " + param.args[0]);

            }
        });
//        findAndHookMethod(ClassLoader.class, "findClass", String.class, new XC_MethodHook() {
//
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                LogUtils.log(TAG + " ClassLoader.findClass " +param.thisObject + " " + param.args[0]);
//
//            }
//        });
        findAndHookMethod(ClassLoader.class, "loadClass", String.class, new XC_MethodHook() {

            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (param.hasThrowable()) {
                    return;
                }
                String className = String.valueOf(param.args[0]);
                Class result = (Class) param.getResult();
                LogUtils.log(TAG + "after ClassLoader.loadClass   " + param.args[0]);
            }
        });


//        findAndHookMethod("java.lang.BootClassLoader", loadPackageParam.classLoader, "findClass", String.class, new XC_MethodHook() {
//
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                LogUtils.log(TAG + " BootClassLoader.findClass " +param.thisObject + " " + param.args[0]);
//
//            }
//        });

//        findAndHookMethod(ClassLoader.class, "loadClass", String.class, new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                if (param.hasThrowable()) return;
//                Class<?> cls = (Class<?>) param.getResult();
//                String name = cls.getName();
//                LogUtils.log(TAG + " loadClass: " + name);
////                if ("foo.ree.demos.x4th01.Base64Util".equals(name)) {
////                    // 所有的类都是通过loadClass方法加载的
////                    // 所以这里通过判断全限定类名，查找到目标类
////                    // 第二步：Hook目标方法
////                    findAndHookMethod(cls, "decrypt", String.class, new XC_MethodHook() {
////                        @Override
////                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
////                            LogUtils.log(param.method + " params: " + Arrays.toString(param.args));
////                        }
////                        @Override
////                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
////                            LogUtils.log(param.method + " return: " + param.getResult());
////                        }
////                    });
////                }
//            }
//        });

    }
}
