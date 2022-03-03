package com.test.hooktest;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import dalvik.system.PathClassLoader;

/**
 * @author duwei on 1/21/22.
 */
public class DelegateClassLoader extends PathClassLoader {

    private ClassLoader mPathClassLoader;

    public DelegateClassLoader(String dexPath, ClassLoader origin) {
        super(dexPath, origin.getParent());
        mPathClassLoader = origin;
    }

//    public DelegateClassLoader(String dexPath, ClassLoader parent) {
//        super(parent);
//        mPathClassLoader = getClass().getClassLoader();
//    }

    //PathClassloader.parent <- PathClassloader  ====> PathClassloader.parent <- Hook ClassLoader <- PathClassloader
    public static synchronized void hook(Context context, ClassLoader pathClassLoader) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
        ClassLoader classLoader = new DelegateClassLoader("", pathClassLoader);
        //线程classloader替换
        Thread.currentThread().setContextClassLoader(classLoader);
//context.getApplicationInfo().sourceDir
//        long start = SystemClock.uptimeMillis();
//        Log.d("xx", "-----------------------DelegateClassLoader  hook ");
//        ClassLoader classLoader = new DelegateClassLoader("", pathClassLoader.getParent());
//        Field parentField = ClassLoader.class.getDeclaredField("parent");
//        parentField.setAccessible(true);
//        parentField.set(pathClassLoader, classLoader);
        //线程classloader替换
//        Thread.currentThread().setContextClassLoader(classLoader);

        //LoadedApk 中classloader替换
        Field packageInfoField = Class.forName("android.app.ContextImpl").getDeclaredField("mPackageInfo");
        packageInfoField.setAccessible(true);
        Object loadedApkObject = packageInfoField.get(context);
        Class LoadedApkClass = Class.forName("android.app.LoadedApk");
        Field appClassLoaderField = LoadedApkClass.getDeclaredField("mClassLoader");
        appClassLoaderField.setAccessible(true);
        ClassLoader oldClassLoader = (ClassLoader) appClassLoaderField.get(loadedApkObject);
        appClassLoaderField.set(loadedApkObject, classLoader);

        Field classLoader1Field = Class.forName("android.app.ContextImpl").getDeclaredField("mClassLoader");
        classLoader1Field.setAccessible(true);
        classLoader1Field.set(context, classLoader);


//        Class loadersClass = Class.forName("android.app.ApplicationLoaders");
//        Field map = loadersClass.getDeclaredField("mLoaders");
//        Field instance = loadersClass.getDeclaredField("gApplicationLoaders");
//        map.setAccessible(true);
//        instance.setAccessible(true);
//        Object loadersObj = instance.get(null);
//
//        ArrayMap map1 = (ArrayMap) map.get(loadersObj);
//        map1.put("/data/app/com.sohu.inputmethod.sogou-1/base.apk", classLoader);
//        map1.size();

        Field dexPathListField = Class.forName("dalvik.system.BaseDexClassLoader").getDeclaredField("pathList");
        dexPathListField.setAccessible(true);
        Object oldD = dexPathListField.get(pathClassLoader);
        dexPathListField.set(classLoader, oldD);

        try {
            classLoader.loadClass("com.sohu.inputmethod.sogou.MainImeServiceDel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Log.d("xx", "");
    }

    /**
     * 缺陷：获取不到部分类加载，只有xml 、 组件定义中用到的类
     * @param name
     * @param resolve
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        long start = System.nanoTime();
        Class<?> c;
        Log.d("xx", "-----------------------DelegateClassLoader loadClass " + name);
        try {
            c = mPathClassLoader.loadClass(name);
            Log.d("xx", "-----------------------DelegateClassLoader loadClass " + name + " cost = " + (System.nanoTime() - start));
            return c;
        } catch (Throwable e) {
            //
        }
        c = super.loadClass(name, resolve);
        Log.d("xx", "-----------------------DelegateClassLoader loadClass " + name + " cost = " + (System.nanoTime() - start));
        return c;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
//        return super.findClass(name);
        long start = System.nanoTime();
        Log.d("xx", "-----------------------DelegateClassLoader  findClass " + name);
        Class c =  super.findClass(name);
        Log.d("xx", "-----------------------DelegateClassLoader  findClass " + name + " cost = " + (System.nanoTime() - start));
        return c;
    }
}
