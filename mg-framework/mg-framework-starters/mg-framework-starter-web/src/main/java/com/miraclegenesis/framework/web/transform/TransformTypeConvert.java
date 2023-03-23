package com.miraclegenesis.framework.web.transform;


/**
 * @author robert
 */
@FunctionalInterface
public interface TransformTypeConvert<T> {

    /**
     * 转换
     * @param object 需要转换的对象
     * @return 转换后的对象
     */
    Object convert(T object);
}
