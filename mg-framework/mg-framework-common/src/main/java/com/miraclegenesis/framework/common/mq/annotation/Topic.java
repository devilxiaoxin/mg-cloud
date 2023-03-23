package com.miraclegenesis.framework.common.mq.annotation;

import java.lang.annotation.*;

/**
 * @author robert
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Topic {
    String value();
}
