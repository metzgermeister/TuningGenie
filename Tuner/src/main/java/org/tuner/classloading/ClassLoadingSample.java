package org.tuner.classloading;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * User: Pavlo_Ivanenko
 * Date: 8/27/12
 * Time: 6:36 AM
 */
public class ClassLoadingSample {

    public void doSmth() throws Exception {
        String fileName = "D:\\java_workspace\\sorting\\Examples\\src\\main\\java\\org\\tuner\\sample\\Example1.java";

        loadAndRun(fileName);
//        Thread.sleep(100000L);
        loadAndRun(fileName);

    }

    private void loadAndRun(String fileName) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, MalformedURLException {
        URL url = new URL("file:d:\\java_workspace\\sorting\\out\\Example1.class");
        ClassLoader parentClassLoader = ReloadableClassLoader.class.getClassLoader();
        ReloadableClassLoader classLoader = new ReloadableClassLoader(parentClassLoader, url);

        Class<?> clazz = Class.forName("org.tuner.sample.Example1", true, classLoader);
        Class<? extends Runnable> runClass = clazz.asSubclass(Runnable.class);
        Constructor<? extends Runnable> ctor = runClass.getConstructor();
        Runnable doRun = ctor.newInstance();
        doRun.run();
    }

    public static void main(String[] args) throws Exception {
        new ClassLoadingSample().doSmth();
    }
}
