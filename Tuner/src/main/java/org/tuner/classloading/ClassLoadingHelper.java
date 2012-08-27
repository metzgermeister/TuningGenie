package org.tuner.classloading;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.*;

/**
 * User: Pavlo_Ivanenko
 * Date: 8/27/12
 * Time: 6:36 AM
 */
public class ClassLoadingHelper {


    public long loadAndRun(String classFileName, String classFilePath, String wrapperFileName, String wrapperFilePath) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, MalformedURLException, ExecutionException, InterruptedException {

        ClassLoader parentClassLoader = ReloadAbleClassLoader.class.getClassLoader();
        ReloadAbleClassLoader classLoader = new ReloadAbleClassLoader(parentClassLoader);

        URL sourceCLassUrl = new URL("file:" + classFilePath);
        classLoader.setUrl(sourceCLassUrl);
        Class.forName(classFileName, true, classLoader);


        URL wrapperCLassUrl = new URL("file:" + wrapperFilePath);
        classLoader.setUrl(wrapperCLassUrl);
        Class<?> wrapperClass = Class.forName(wrapperFileName, true, classLoader);

        Class<? extends Callable> runClass = wrapperClass.asSubclass(Callable.class);
        Constructor<? extends Callable> constructor = runClass.getConstructor();
        Callable<Long> callable = constructor.newInstance();


        ExecutorService pool = Executors.newFixedThreadPool(3);
        Future<Long> future = pool.submit(callable);

        return future.get();
    }

    public static void main(String[] args) throws Exception {
        new ClassLoadingHelper().loadAndRun("org.tuner.sample.EnhancedQuickSort",
                "d:/java_workspace/sorting/Examples/out/org/tuner/sample/EnhancedQuickSort.class",
                "org.tuner.sample.EnhancedQuickSortWrapper",
                "d:/java_workspace/sorting/Examples/out/org/tuner/sample/EnhancedQuickSortWrapper.class"
                );
    }
}
