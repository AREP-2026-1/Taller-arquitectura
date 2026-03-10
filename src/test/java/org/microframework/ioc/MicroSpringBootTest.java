package org.microframework.ioc;

import org.junit.Test;
import org.microframework.annotation.GetMapping;
import org.microframework.annotation.RequestParam;
import org.microframework.annotation.RestController;
import org.microframework.server.HttpServer;
import org.microframework.server.Request;
import org.microframework.server.Response;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Unit tests for MicroSpringBoot IoC container and ComponentScanner.
 */
public class MicroSpringBootTest {

    // ---------------------------------------------------------------
    // Minimal in-test controller fixtures
    // ---------------------------------------------------------------

    @RestController
    public static class SimpleController {
        @GetMapping("/simple")
        public String simple() {
            return "simple-response";
        }
    }

    @RestController
    public static class ParamController {
        @GetMapping("/greet")
        public String greet(@RequestParam(value = "name", defaultValue = "Mundo") String name) {
            return "Hola " + name;
        }
    }

    public static class NotAController {
        @GetMapping("/ignored")
        public String ignored() {
            return "should not be registered";
        }
    }

    // ---------------------------------------------------------------
    // loadController tests
    // ---------------------------------------------------------------

    @Test
    public void loadControllerRegistersGetMappingRoute() throws Exception {
        MicroSpringBoot.loadController(SimpleController.class);
        HttpServer server = MicroSpringBoot.getServer();
        assertTrue("Route /simple must be registered",
                server.getGetRoutes().containsKey("/simple"));
    }

    @Test
    public void routeHandlerReturnsExpectedBody() throws Exception {
        MicroSpringBoot.loadController(SimpleController.class);
        HttpServer server = MicroSpringBoot.getServer();

        Request req = new Request("GET", "/simple", Map.of(), Map.of());
        Response res = new Response();
        String body = server.getGetRoutes().get("/simple").handle(req, res);
        assertEquals("simple-response", body);
    }

    @Test
    public void requestParamIsInjectedFromQueryString() throws Exception {
        MicroSpringBoot.loadController(ParamController.class);
        HttpServer server = MicroSpringBoot.getServer();

        Request req = new Request("GET", "/greet", Map.of("name", "Ana"), Map.of());
        Response res = new Response();
        String body = server.getGetRoutes().get("/greet").handle(req, res);
        assertEquals("Hola Ana", body);
    }

    @Test
    public void requestParamUsesDefaultValueWhenAbsent() throws Exception {
        MicroSpringBoot.loadController(ParamController.class);
        HttpServer server = MicroSpringBoot.getServer();

        Request req = new Request("GET", "/greet", Map.of(), Map.of());
        Response res = new Response();
        String body = server.getGetRoutes().get("/greet").handle(req, res);
        assertEquals("Hola Mundo", body);
    }

    @Test
    public void loadControllerSkipsClassWithoutAnnotation() throws Exception {
        HttpServer server = MicroSpringBoot.getServer();
        int routesBefore = server.getGetRoutes().size();
        MicroSpringBoot.loadController(NotAController.class);
        assertEquals("No new route should be added for a non-@RestController",
                routesBefore, server.getGetRoutes().size());
    }

    // ---------------------------------------------------------------
    // ComponentScanner test
    // ---------------------------------------------------------------

    @Test
    public void componentScannerFindsRestControllers() throws Exception {
        List<Class<?>> controllers = ComponentScanner.findRestControllers();
        // At minimum our two example controllers must be discovered
        boolean foundHello = controllers.stream()
                .anyMatch(c -> c.getSimpleName().equals("HelloController"));
        boolean foundGreeting = controllers.stream()
                .anyMatch(c -> c.getSimpleName().equals("GreetingController"));
        assertTrue("HelloController must be discovered", foundHello);
        assertTrue("GreetingController must be discovered", foundGreeting);
    }
}
