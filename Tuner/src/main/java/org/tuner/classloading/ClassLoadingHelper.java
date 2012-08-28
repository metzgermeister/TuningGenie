package org.tuner.classloading;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * User: Pavlo_Ivanenko
 * Date: 8/27/12
 * Time: 6:36 AM
 */
public class ClassLoadingHelper {
    private ExecutorService pool = Executors.newFixedThreadPool(1);


    public long loadAndRun(String className, String classFilePath, String wrapperName, String wrapperFilePath) throws Exception {

        ClassLoader parentClassLoader = ReloadAbleClassLoader.class.getClassLoader();
        ReloadAbleClassLoader classLoader = new ReloadAbleClassLoader(parentClassLoader);

        URL sourceCLassUrl = new URL("file:" + classFilePath);
        classLoader.setUrl(sourceCLassUrl);
        Class.forName(className, true, classLoader);


        URL wrapperCLassUrl = new URL("file:" + wrapperFilePath);
        classLoader.setUrl(wrapperCLassUrl);
        Class<?> wrapperClass = Class.forName(wrapperName, true, classLoader);

        Class<? extends Callable> runClass = wrapperClass.asSubclass(Callable.class);
        Constructor<? extends Callable> constructor = runClass.getConstructor();
        Callable<Long> callable = constructor.newInstance();

        Future<Long> future = pool.submit(callable);

        return future.get();
    }

    public static void main(String[] args) throws Exception {
        long executionTime = new ClassLoadingHelper().loadAndRun("org.tuner.sample.EnhancedQuickSort",
                "d:/java_workspace/sorting/Examples/out/org/tuner/sample/EnhancedQuickSort.class",
                "org.tuner.sample.EnhancedQuickSortWrapper",
                "d:/java_workspace/sorting/Examples/out/org/tuner/sample/EnhancedQuickSortWrapper.class"
        );
        System.out.println(executionTime);
        System.exit(42);
    }
}
