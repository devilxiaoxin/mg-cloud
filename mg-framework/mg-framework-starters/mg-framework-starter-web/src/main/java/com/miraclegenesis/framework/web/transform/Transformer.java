package com.miraclegenesis.framework.web.transform;

/**
 * @author robert
 */
public interface Transformer<T> {

    /**
     * 翻译
     *
     * @param original 原始值
     * @param datasource 翻译数据源
     * @param param 翻译后取值参数名字
     * @return 翻译后的值
     */
    String transform(T original, Class<?> datasource, String param);
}
