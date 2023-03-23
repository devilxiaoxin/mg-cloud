package com.miraclegenesis.framework.mysql.shard;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author robert
 */
@Target({ElementType.FIELD})
@Retention(RUNTIME)
public @interface TableShardField {
}
