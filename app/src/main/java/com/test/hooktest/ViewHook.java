package com.test.hooktest;

import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.inputmethodservice.InputMethodService;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.test.hooktest.utils.LogUtils;
import com.test.hooktest.utils.Util;

import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedBridge.hookAllConstructors;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

/**
 * Created by @author dw
 * Date : 20-5-27
 */
public class ViewHook extends XC_MethodHook {

    public static final String TAG = "ViewHook      :";

    public static void initAllHooks(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws NoSuchFieldException {

//        final Class<?> captureViewClass = findClass("com.sohu.inputmethod.sogou.feedback.TextCaptureView", loadPackageParam.classLoader);
//
//        findAndHookMethod(View.class, "setFrame", int.class, int.class, int.class, int.class, new XC_MethodHook() {
//
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                if (captureViewClass.isAssignableFrom(param.thisObject.getClass())) {
//                    LogUtils.log(TAG +  "setFrame(" +param.args[0]+"," +param.args[1]+ "," +param.args[2]+ "," +param.args[3]+ ") ");
//                    Util.showBackTrace();
//                }
//
//            }
//        });
//        findAndHookMethod(View.class, "dispatchTouchEvent", MotionEvent.class, new XC_MethodHook() {
//
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                MotionEvent event = (MotionEvent) param.args[0];
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    LogUtils.log(TAG +  param.thisObject+" dispatchTouchEvent(" +event.getX()+"," +event.getY()+ ") ");
//                }
//
//            }
//        });
        findAndHookMethod(View.class, "isShown", new XC_MethodHook() {

            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                LogUtils.log(TAG + param.thisObject + " " + param.thisObject.getClass() + " isShown");
                Util.showBackTrace();

            }
        });
        findAndHookMethod(InputMethodService.class, "isInputViewShown", new XC_MethodHook() {

            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                LogUtils.log(TAG + param.thisObject + "  isInputViewShown");
                Util.showBackTrace();

            }
        });
//        findAndHookMethod(ViewGroup.class, "dispatchTouchEvent", MotionEvent.class, new XC_MethodHook() {
//
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                MotionEvent event = (MotionEvent) param.args[0];
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    LogUtils.log(TAG +  param.thisObject+" dispatchTouchEvent(" +event.getX()+"," +event.getY()+ ") ");
//                }
//
//            }
//        });
//        final Class<?> virtualview = findClass("com.sogou.virtual_view.virtualview.VirtualView", loadPackageParam.classLoader);
//        findAndHookMethod(virtualview, "dispatchTouchEvent", MotionEvent.class, new XC_MethodHook() {
//
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                MotionEvent event = (MotionEvent) param.args[0];
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    LogUtils.log(TAG +  param.thisObject+" dispatchTouchEvent(" +event.getX()+"," +event.getY()+ ") ");
//                }
//
//            }
//        });
//        final Class<?> virtualViewGroup = findClass("com.sohu.inputmethod.sogou.VirtualViewGroup", loadPackageParam.classLoader);
//        findAndHookMethod(virtualViewGroup, "dispatchTouchEvent", MotionEvent.class, new XC_MethodHook() {
//
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                MotionEvent event = (MotionEvent) param.args[0];
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    LogUtils.log(TAG +  param.thisObject+" dispatchTouchEvent(" +event.getX()+"," +event.getY()+ ") ");
//                }
//
//            }
//        });
//        final Class<?> viewRoot = findClass("android.view.ViewRootImpl", loadPackageParam.classLoader);
//        final Field layoutRequest = viewRoot.getDeclaredField("mLayoutRequested");
//
//        findAndHookMethod(viewRoot, "performLayout", WindowManager.LayoutParams.class, int.class, int.class, new XC_MethodHook() {
//
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                LogUtils.log(TAG +  param.thisObject+" performLayout " +param.thisObject);
//            }
//        });
//
//        findAndHookMethod(viewRoot, "performTraversals", new XC_MethodHook() {
//
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                Object value = layoutRequest.get(param.thisObject);
//                LogUtils.log(TAG +  param.thisObject+" performTraversals " +param.thisObject+ " value="+value);
//            }
//        });
//
//        try {
//            hookAllConstructors(Thread.class,  new XC_MethodHook() {
//
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    LogUtils.log(TAG + param.thisObject + "  Thread " + param.thisObject);
//                    Util.showBackTrace();
//                }
//            });
//        } catch (Error e) {
//            LogUtils.log(e.toString());
//        }


//            findAndHookMethod(Bitmap.class, "recycle", new XC_MethodHook() {
//
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                LogUtils.log(TAG +  param.thisObject+" recycle " +param.thisObject);
//                Util.showBackTrace();
//            }
//
//        });
//
//        final Class<?> lruBitmapPoolRoot = findClass("com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool", loadPackageParam.classLoader);
//
//        findAndHookMethod(lruBitmapPoolRoot, "put", Bitmap.class, new XC_MethodHook() {
//
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                LogUtils.log(TAG +  param.thisObject+"  LruBitmapPool put  bitmap=" +param.args[0]);
//                Util.showBackTrace();
//            }
//        });

    }
}
