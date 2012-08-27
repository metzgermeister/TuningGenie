package org.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * User: Pavlo_Ivanenko
 * Date: 8/27/12
 * Time: 6:36 AM
 */
public class ClassLoadingTest {

    public void doSmth() throws Exception {
        String fileName = "D:\\java_workspace\\sorting\\Tuner\\src\\main\\java\\org\\test\\sample\\Example1.java";

        loadAndRun(fileName);
//        Thread.sleep(100000L);
        loadAndRun(fileName);

    }

    private void loadAndRun(String fileName) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, MalformedURLException {
//        URL url = new URL("file:D:/java_workspace/sorting/Tuner/src/main/java/org/test/sample/Example1.java");
        URL url = new URL("file:d:\\java_workspace\\sorting\\out\\Example1.class");
        ClassLoader parentClassLoader = ReloadingClassLoader.class.getClassLoader();
        ReloadingClassLoader classLoader = new ReloadingClassLoader(parentClassLoader, url);

        Class<?> clazz = Class.forName("org.test.sample.Example1", true, classLoader);
        Class<? extends Runnable> runClass = clazz.asSubclass(Runnable.class);
        Constructor<? extends Runnable> ctor = runClass.getConstructor();
        Runnable doRun = ctor.newInstance();
        doRun.run();
//
//

//        URL systemResource = ClassLoader.getSystemClassLoader().getResource(fileName);
//        System.out.println(fileName);
//        ClassLoader loader = URLClassLoader.newInstance(
//                new URL[]{systemResource},
//                getClass().getClassLoader()
//        );
//        Class<?> clazz = Class.forName("org.test.sample.Example1", true, loader);
//        Class<? extends Runnable> runClass = clazz.asSubclass(Runnable.class);
//        Constructor<? extends Runnable> ctor = runClass.getConstructor();
//        Runnable doRun = ctor.newInstance();
//        doRun.run();
    }

    public static void main(String[] args) throws Exception {
        new ClassLoadingTest().doSmth();
    }
}
