package org.tuner.classloading;

import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
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
    private ReloadableClassLoader classLoader;
    
    public ClassLoadingHelper() {
        ClassLoader parentClassLoader = ReloadableClassLoader.class.getClassLoader();
        classLoader = new ReloadableClassLoader(parentClassLoader);
    }
    
    public ClassLoadingHelper(ClassLoader parent) {
        classLoader = new ReloadableClassLoader(parent);
    }
    
    public long loadAndRun(ClassDefinition wrapper, ClassDefinition... definitions) throws Exception {
        load(definitions);
        
        URL wrapperCLassUrl = new URL("file:" + wrapper.getClassFilePath());
        classLoader.setUrl(wrapperCLassUrl);
        Class<?> wrapperClass = Class.forName(wrapper.getClassName(), true, classLoader);
        
        Class<? extends Callable> runClass = wrapperClass.asSubclass(Callable.class);
        Constructor<? extends Callable> constructor = runClass.getConstructor();
        Callable<Long> callable = constructor.newInstance();
        
        Future<Long> future = pool.submit(callable);
        
        return future.get();
    }
    
    public void load(ClassDefinition[] definitions) throws MalformedURLException, ClassNotFoundException {
        for (ClassDefinition definition : definitions) {
            load(definition);
        }
    }
    
    public void load(ClassDefinition definition) throws MalformedURLException, ClassNotFoundException {
        URL sourceCLassUrl = new URL("file:" + definition.getClassFilePath());
        classLoader.setUrl(sourceCLassUrl);
        Class.forName(definition.getClassName(), true, classLoader);
    }
    
    
    public ReloadableClassLoader getClassLoader() {
        return classLoader;
    }
    
    public static void main(String[] args) throws Exception {
        ClassDefinition wrapper = new ClassDefinition("org.tuner.sample.EnhancedQuickSortWrapper",
                "/Users/metzgermeister/projects/TuningGenie/Examples/out/org/tuner/sample/EnhancedQuickSortWrapper.class");
        long executionTime = new ClassLoadingHelper().loadAndRun(wrapper, new ClassDefinition("org.tuner.sample.EnhancedQuickSort",
                "/Users/metzgermeister/projects/TuningGenie/Examples/out/org/tuner/sample/EnhancedQuickSort.class")
        
        );
        System.out.println(executionTime);
        System.exit(42);
    }
}
