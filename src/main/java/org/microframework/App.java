package org.microframework;

import static org.microframework.server.MicroFramework.*;

/**
 * Example application demonstrating the MicroFramework web framework.
 *
 * REST endpoints:
 *   GET /hello?name=Pedro  -> "Hello Pedro"
 *   GET /pi                -> "3.141592653589793"
 *
 * Static files served from /webroot (e.g., /index.html, /styles.css, /app.js)
 */
public class App {

    public static void main(String[] args) {
        staticfiles("/webroot");

        get("/hello", (req, res) -> "Hello " + req.getValues("name"));

        get("/pi", (req, res) -> {
            return String.valueOf(Math.PI);
        });

        start();
    }
}
