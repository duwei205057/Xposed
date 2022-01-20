package com.test.hooktest;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Message;
import android.os.MessageQueue;
import android.view.Choreographer;
import android.view.View;
import android.view.ViewParent;

import com.test.hooktest.utils.LogUtils;
import com.test.hooktest.utils.Util;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

/**
 * Created by @author dw
 * Date : 20-5-27
 */
public class ChoreographerHook extends XC_MethodHook {

    public static final String TAG = "ChoreographerHook      :";

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
        final Class<?> viewRoot = findClass("android.view.ViewRootImpl", loadPackageParam.classLoader);
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

//        findAndHookMethod(viewRoot, "scheduleTraversals", new XC_MethodHook() {
//
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                LogUtils.log(TAG +  param.thisObject+" --------------scheduleTraversals ");
//                Util.showBackTrace();
//            }
//        });

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



        findAndHookMethod(MessageQueue.class, "enqueueMessage", Message.class, long.class, new XC_MethodHook() {

            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                LogUtils.log(TAG +  param.thisObject+" enqueueMessage   message=" +param.args[0] + " when = "+param.args[1]);
                if (param.args[0] instanceof Message) {
                    Message message = (Message) param.args[0];
                    if (message.obj instanceof AnimationDrawable) {
                        AnimationDrawable animationDrawable = (AnimationDrawable) message.obj;
                        if (animationDrawable.getCallback() instanceof View) {
                            View view = (View) animationDrawable.getCallback();
                            ViewParent viewParent = view.getParent();
                            while (viewParent != null) {
                                LogUtils.log(TAG +  param.thisObject+" enqueueMessage   viewParent=" +viewParent);
                                viewParent = viewParent.getParent();
                            }
                        }
                    }
                }
            }
        });
        findAndHookMethod(MessageQueue.class, "postSyncBarrier", long.class, new XC_MethodHook() {

            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                LogUtils.log(TAG +  param.thisObject+"  postSyncBarrier  when=" +param.args[0] + " token = " + param.getResult());
                Util.showBackTrace();
            }
        });
        findAndHookMethod(MessageQueue.class, "removeSyncBarrier", int.class, new XC_MethodHook() {

            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                LogUtils.log(TAG +  param.thisObject+"  removeSyncBarrier  token=" +param.args[0]);
            }
        });
        findAndHookMethod(Choreographer.class, "postFrameCallback", Choreographer.FrameCallback.class, new XC_MethodHook() {

            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                LogUtils.log(TAG +  param.thisObject+"  postFrameCallback  ");
                Util.showBackTrace();
            }
        });

    }
}
