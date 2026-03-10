package org.microframework.ioc;

import org.microframework.annotation.RestController;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Scans the classpath at runtime to find all classes annotated with @RestController.
 * Uses Java reflection to inspect loaded classes and discover web components
 * without requiring explicit registration.
 */
public class ComponentScanner {

    /**
     * Scans all classpath roots for classes annotated with @RestController.
     *
     * @return list of discovered controller classes
     */
    public static List<Class<?>> findRestControllers() throws Exception {
        List<Class<?>> controllers = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        Enumeration<URL> roots = classLoader.getResources("");
        while (roots.hasMoreElements()) {
            URL root = roots.nextElement();
            if ("file".equals(root.getProtocol())) {
                File rootDir = new File(root.toURI());
                if (rootDir.isDirectory()) {
                    scanDirectory(rootDir, "", classLoader, controllers);
                }
            }
        }
        return controllers;
    }

    private static void scanDirectory(File dir, String packagePrefix,
                                      ClassLoader classLoader,
                                      List<Class<?>> controllers) {
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                String subPackage = packagePrefix.isEmpty()
                        ? file.getName()
                        : packagePrefix + "." + file.getName();
                scanDirectory(file, subPackage, classLoader, controllers);
            } else if (file.getName().endsWith(".class")) {
                String simpleName = file.getName().replace(".class", "");
                String className = packagePrefix.isEmpty()
                        ? simpleName
                        : packagePrefix + "." + simpleName;
                try {
                    Class<?> clazz = classLoader.loadClass(className);
                    if (clazz.isAnnotationPresent(RestController.class)) {
                        controllers.add(clazz);
                        System.out.println("Discovered @RestController: " + className);
                    }
                } catch (ClassNotFoundException | NoClassDefFoundError ignored) {
                    // Skip classes that cannot be loaded
                }
            }
        }
    }
}
