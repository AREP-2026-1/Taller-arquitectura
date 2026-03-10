package org.microframework.controller;

import org.microframework.annotation.GetMapping;
import org.microframework.annotation.RestController;

/**
 * Simple REST controller demonstrating @RestController and @GetMapping.
 * Auto-discovered by MicroSpringBoot when scanning the classpath.
 */
@RestController
public class HelloController {

    @GetMapping("/")
    public String index() {
        return "Greetings from MicroSpringBoot!";
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }
}
