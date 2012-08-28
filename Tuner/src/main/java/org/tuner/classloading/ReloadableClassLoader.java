package org.tuner.classloading;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * User: Pavlo_Ivanenko
 * Date: 8/27/12
 * Time: 7:24 AM
 */
public class ReloadAbleClassLoader extends ClassLoader {
    private URL url;

    public ReloadAbleClassLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    public Class loadClass(String name) throws ClassNotFoundException {
        if (shouldBeLoadedBySuperClassLoader(name))
            return super.loadClass(name);

        try {

            URLConnection connection = url.openConnection();
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

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean shouldBeLoadedBySuperClassLoader(String name) {
//        return "java.lang.Runnable".equals(name) || "java.util.concurrent.Callable".equals(name);
        return !name.startsWith("org.tuner.sample");
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
}
