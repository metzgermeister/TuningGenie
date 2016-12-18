package org.tuner.classloading;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * User: Pavlo_Ivanenko
 * Date: 8/27/12
 * Time: 7:24 AM
 * <p>
 * not thread safe
 */
public class ReloadableClassLoader extends ClassLoader {
    private URL url;
    
    public ReloadableClassLoader(ClassLoader parent) {
        super(parent);
    }
    
    @Override
    public Class loadClass(String name) throws ClassNotFoundException {
        if (shouldBeLoadedBySuperClassLoader(name))
            return super.loadClass(name);
        
        if (shouldBeLoadedOnlyOnce(name)) {
            try {
                return getParent().loadClass(name);
            } catch (ClassNotFoundException ex) {
                //parent can't load
            }
            
            Class<?> loadedClass = findLoadedClass(name);
            if (loadedClass != null) {
                return loadedClass;
            }
        }
        try {
            
            URLConnection connection = getConnection(name);
            InputStream input = connection.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int data = input.read();
            
            while (data != -1) {
                buffer.write(data);
                data = input.read();
            }
            
            input.close();
            
            byte[] classData = buffer.toByteArray();
            
            return defineClass(name, classData, 0, classData.length);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    private URLConnection getConnection(String name) throws IOException {
        if (name.contains("$")) {
            String path = url.getPath();
            String innerClassName = name.substring(name.indexOf("$"));
            String innerClassPath = path.replace(".class", innerClassName + ".class");
            return new URL("file:" + innerClassPath).openConnection();
        } else {
            return url.openConnection();
        }
    }
    
    private boolean shouldBeLoadedBySuperClassLoader(String name) {
//        return "java.lang.Runnable".equals(name) || "java.util.concurrent.Callable".equals(name);
        return !name.startsWith("org.tuner.sample");
    }
    
    private boolean shouldBeLoadedOnlyOnce(String name) {
        return "org.tuner.sample.PoolCache".equals(name);
    }
    
    public URL getUrl() {
        return url;
    }
    
    public void setUrl(URL url) {
        this.url = url;
    }
}
