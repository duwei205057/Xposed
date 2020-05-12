package com.test.hooktest;

import com.test.hooktest.utils.LogUtils;
import com.test.hooktest.utils.Util;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by acpm on 21/11/15.
 */
public class HashHook extends XC_MethodHook {

    public static final String TAG = "MessageDigest      :";

    public static void initAllHooks(final XC_LoadPackage.LoadPackageParam loadPackageParam) {

        findAndHookMethod(MessageDigest.class, "getInstance", String.class, new XC_MethodHook() {

            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                LogUtils.log(TAG +  "Algorithm(" +param.args[0]+") [");
            }
        });

        findAndHookMethod(MessageDigest.class, "update", byte[].class, new XC_MethodHook() {

            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                LogUtils.log(TAG + Util.byteArrayToString((byte[]) param.args[0])+" : ");
            }

        });

        findAndHookMethod(MessageDigest.class, "update", byte[].class, "int", "int", new XC_MethodHook() {

            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                LogUtils.log(TAG + Util.byteArrayToString((byte[]) param.args[0])+" : ");
            }

        });

        findAndHookMethod(MessageDigest.class, "update", ByteBuffer.class, new XC_MethodHook() {

            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                ByteBuffer bb = (ByteBuffer) param.args[0];

                LogUtils.log(TAG + Util.byteArrayToString(bb.array()) + " : ");
            }
        });

        //the computed one way hash value
        findAndHookMethod(MessageDigest.class, "digest", new XC_MethodHook() {

            protected void afterHookedMethod(MethodHookParam param) throws Throwable {


                LogUtils.log(TAG + Util.toHexString((byte[]) param.getResult())+"]");
            }
        });

        findAndHookMethod(MessageDigest.class, "digest", byte[].class, new XC_MethodHook() {

            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                //XposedBridge.log(TAG + "digest2 = " + Util.byteArrayToString((byte[]) param.args[0]));

                //sb.append(" : " + Util.toHexString((byte[]) param.getResult())+"]");

                //XposedBridge.log(TAG + sb.toString());
                //sb = new StringBuffer();
            }
        });

        findAndHookMethod(MessageDigest.class, "digest", byte[].class, "int", "int", new XC_MethodHook() {

            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                //sb.append(" : " + (Integer) param.getResult()+"]");
                //XposedBridge.log(TAG + sb.toString());
                //sb = new StringBuffer();
            }
        });
    }
}
