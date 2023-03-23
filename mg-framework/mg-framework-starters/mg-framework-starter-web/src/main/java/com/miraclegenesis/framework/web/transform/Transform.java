package com.miraclegenesis.framework.web.transform;

import java.lang.annotation.*;

/**
 * @author robert
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Transform {

    Class<? extends Transformer> transformer() default Transformer.class;

    Class<?> dataSource() default Void.class;

    /**
     * 默认是当前字段或“字段+Code”或“字段+Id”
     */
    String from() default "";

    String param() default "";

}
