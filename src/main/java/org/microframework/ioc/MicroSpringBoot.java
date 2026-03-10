package org.microframework.ioc;

import org.microframework.annotation.GetMapping;
import org.microframework.annotation.RequestParam;
import org.microframework.annotation.RestController;
import org.microframework.server.HttpServer;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

/**
 * IoC container and main entry point for the annotation-based web framework.
 *
 * <p>Two startup modes:
 * <ol>
 *   <li>Pass a fully-qualified class name as argument to load a single controller:
 *       <pre>java -cp target/classes org.microframework.ioc.MicroSpringBoot org.microframework.controller.HelloController</pre>
 *   </li>
 *   <li>Run without arguments to auto-scan the classpath for all @RestController classes.</li>
 * </ol>
 *
 * <p>For every discovered controller the framework:
 * <ul>
 *   <li>Instantiates it via its no-arg constructor (using reflection)</li>
 *   <li>Registers each @GetMapping method as an HTTP GET route</li>
 *   <li>Injects @RequestParam query parameters into method arguments at request time</li>
 * </ul>
 */
public class MicroSpringBoot {

    private static final HttpServer server = new HttpServer();

    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            // Mode 1: load a specific controller class given on the command line
            String className = args[0];
            System.out.println("Loading controller from argument: " + className);
            Class<?> clazz = Class.forName(className);
            loadController(clazz);
        } else {
            // Mode 2: auto-discover all @RestController classes in the classpath
            System.out.println("Scanning classpath for @RestController components...");
            List<Class<?>> controllers = ComponentScanner.findRestControllers();
            if (controllers.isEmpty()) {
                System.out.println("No @RestController classes found. Exiting.");
                return;
            }
            for (Class<?> controller : controllers) {
                loadController(controller);
            }
        }

        server.setStaticFilesPath("/webroot");
        server.start();
    }

    /**
     * Instantiates the controller and registers all of its @GetMapping methods
     * as HTTP routes, wiring @RequestParam parameters to query string values.
     */
    static void loadController(Class<?> controllerClass) throws Exception {
        if (!controllerClass.isAnnotationPresent(RestController.class)) {
            System.out.println("Skipping " + controllerClass.getName()
                    + " — not annotated with @RestController");
            return;
        }

        Object instance = controllerClass.getDeclaredConstructor().newInstance();

        for (Method method : controllerClass.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(GetMapping.class)) continue;

            String path = method.getAnnotation(GetMapping.class).value();

            server.addGetRoute(path, (req, res) -> {
                try {
                    Parameter[] params = method.getParameters();
                    Object[] arguments = new Object[params.length];

                    for (int i = 0; i < params.length; i++) {
                        if (params[i].isAnnotationPresent(RequestParam.class)) {
                            RequestParam rp = params[i].getAnnotation(RequestParam.class);
                            String value = req.getValues(rp.value());
                            arguments[i] = (value != null && !value.isEmpty())
                                    ? value
                                    : rp.defaultValue();
                        } else {
                            arguments[i] = null;
                        }
                    }

                    return (String) method.invoke(instance, arguments);
                } catch (Exception e) {
                    return "Internal Server Error: " + e.getCause().getMessage();
                }
            });

            System.out.println("Mapped GET " + path
                    + " -> " + controllerClass.getSimpleName() + "." + method.getName() + "()");
        }
    }

    /** Package-private accessor used in tests. */
    static HttpServer getServer() {
        return server;
    }
}
