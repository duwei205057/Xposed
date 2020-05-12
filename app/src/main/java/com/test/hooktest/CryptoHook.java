package com.test.hooktest;

import com.test.hooktest.utils.LogUtils;
import com.test.hooktest.utils.Util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by acpm on 16/11/15.
 */
public class CryptoHook extends XC_MethodHook {

    public static final String TAG = "Crypto   :";

    public static void initAllHooks(final XC_LoadPackage.LoadPackageParam loadPackageParam) {

//        findAndHookConstructor(SecretKeySpec.class, byte[].class, String.class, new XC_MethodHook() {
//
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//
//                LogUtils.log(TAG + "SecretKeySpec(" + Util.byteArrayToString((byte[]) param.args[0]) + ","+(String) param.args[1]+")");
//            }
//
//        });

        findAndHookMethod(Cipher.class, "doFinal", byte[].class, new XC_MethodHook() {

            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                StringBuffer   sb = new StringBuffer();
                sb.append(" (" + Util.byteArrayToString((byte[]) param.args[0]) + " , ");
                sb.append(Util.byteArrayToString((byte[]) param.getResult()) + ")");

                LogUtils.log(TAG +  sb.toString());
                StackTraceElement[] stes = Thread.currentThread().getStackTrace();
                for (StackTraceElement ste : stes) {
                    LogUtils.log(TAG + ste.getClassName()+ " " + ste.getMethodName()+" "+ ste.getLineNumber());
                }
            }

        });

//        findAndHookMethod(Cipher.class, "getIV", new XC_MethodHook() {
//
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//
//                LogUtils.log(TAG + " IV:" + (String) param.getResult());
//            }
//
//        });
//
//        findAndHookConstructor(IvParameterSpec.class, byte[].class, new XC_MethodHook() {
//
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//
//                LogUtils.log(TAG + " IV: " + Util.byteArrayToString((byte[]) param.args[0]));
//            }
//        });
//
//        findAndHookMethod(SecureRandom.class, "setSeed", byte[].class, new XC_MethodHook() {
//
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//
//                LogUtils.log(TAG + " Seed:" + Util.byteArrayToString((byte[]) param.args[0]));
//            }
//
//        });

        findAndHookMethod(Cipher.class, "getInstance", String.class, new XC_MethodHook() {

            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                //Transformation ex AES/CBC/PKCS7Padding
                LogUtils.log(TAG + " , Cipher[" + (String) param.args[0] + "] ");
            }

        });

//        findAndHookConstructor(PBEKeySpec.class, char[].class, byte[].class, int.class, int.class, new XC_MethodHook() {
//
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                LogUtils.log(TAG + "[PBEKeySpec] - Password: " + String.valueOf((char[])param.args[0]) + " || Salt: " +  Util.byteArrayToString((byte[])param.args[1]));
//            }
//        });
    }
}
