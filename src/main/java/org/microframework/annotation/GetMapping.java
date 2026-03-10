package org.microframework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Maps a method to a GET HTTP endpoint at the specified URI path.
 * Only supported on methods inside a @RestController class.
 * Return type must be String.
 *
 * Example:
 * <pre>
 *     @GetMapping("/hello")
 *     public String hello() {
 *         return "Hello, World!";
 *     }
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GetMapping {
    String value();
}
