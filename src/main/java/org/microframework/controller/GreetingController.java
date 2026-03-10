package org.microframework.controller;

import org.microframework.annotation.GetMapping;
import org.microframework.annotation.RequestParam;
import org.microframework.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Demonstrates @RequestParam with a default value.
 * GET /greeting           -> "Hola World"
 * GET /greeting?name=Ana  -> "Hola Ana"
 */
@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hola " + name;
    }

    @GetMapping("/counter")
    public String counter() {
        return "Request count: " + counter.incrementAndGet();
    }
}
