package com.miraclegenesis.framework.web.result;


import java.lang.annotation.*;

/**
 * @author robert
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface IgnoredResultWrapper {
}
