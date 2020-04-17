package com.test.hooktest.utils;

import android.util.Log;

import de.robv.android.xposed.XposedBridge;

/**
 * Created by @author dw
 * Date : 20-4-15
 */
public class LogUtils {

    public static void log(String message) {
        Log.d("mm", message);
        XposedBridge.log(message);
    }

}
