package com.test.hooktest;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.HashSet;

import dalvik.system.PathClassLoader;

/**
 * @author duwei on 1/21/22.
 */
public class DelegateClassLoader2 extends PathClassLoader {

    private ClassLoader mPathClassLoader;

    public DelegateClassLoader2(String dexPath, ClassLoader pathClassLoader) {
        super(dexPath, pathClassLoader.getParent());
        mPathClassLoader = getClass().getClassLoader();
    }

    public DelegateClassLoader2(String dexPath, String librarySearchPath, ClassLoader parent) {
        super(dexPath, librarySearchPath, parent);
        mPathClassLoader = getClass().getClassLoader();
    }

    //PathClassloader.parent <- PathClassloader  ====> PathClassloader.parent <- Hook ClassLoader <- PathClassloader
    public static synchronized void hook(Context context, ClassLoader pathClassLoader) throws NoSuchFieldException, IllegalAccessException {

        /**
         * 预加载System Log类  注意 ""+String 会用到StringBuilder类 也需要预加载
         * 不进行预加载的话，在自定义的classLoader.loadClass中因为用到了以上的类，会出现死循环调用
         */
        System.nanoTime();
        String a = "aa";
        Log.d("xx", "-----------------------DelegateClassLoader2  hook " + a + context);
        ClassLoader classLoader = new DelegateClassLoader2("", pathClassLoader);//context.getApplicationInfo().sourceDir
        Field parentField = ClassLoader.class.getDeclaredField("parent");
        parentField.setAccessible(true);
        parentField.set(pathClassLoader, classLoader);
//        Thread.currentThread().setContextClassLoader(classLoader);
    }

    HashSet<String> classSet = new HashSet();

    /**
     * 缺陷：加载不到工程的类（但会有工程类的类名传入）
     * @param name
     * @param resolve
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
//        if (classSet.contains(name)) {
//            return null;
//        } else {
//            classSet.add(name);
//        }
        long start = System.nanoTime();
        Class<?> c;
        Log.d("xx", "-----------------------DelegateClassLoader2  loadClass " + name);
//        if (name.contains("MainImeServiceDel")) {
//            try {
//                c = findClass(name);
//                Log.d("xx", "-----------------------DelegateClassLoader loadClass " + name + " cost = " + (System.nanoTime() - start));
//                return c;
//            } catch (Throwable e) {
//                //
//            }
//        }

        c = super.loadClass(name, resolve);
        Log.d("xx", "-----------------------DelegateClassLoader2  loadClass " + name + " cost = " + (System.nanoTime() - start));
        return c;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }
}
