package org.microframework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Binds a method parameter to an HTTP query parameter.
 * If the query parameter is absent, the defaultValue is used.
 *
 * Example:
 * <pre>
 *     @GetMapping("/greeting")
 *     public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
 *         return "Hello, " + name;
 *     }
 * </pre>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestParam {
    String value();
    String defaultValue() default "";
}
