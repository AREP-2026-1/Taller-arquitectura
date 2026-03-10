package org.microframework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as a REST controller component.
 * Classes annotated with this will be auto-discovered by MicroSpringBoot
 * and their @GetMapping methods will be registered as HTTP endpoints.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestController {
}
