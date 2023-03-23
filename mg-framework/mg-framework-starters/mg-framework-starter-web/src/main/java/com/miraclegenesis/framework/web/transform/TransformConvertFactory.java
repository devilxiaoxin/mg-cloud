package com.miraclegenesis.framework.web.transform;

import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author robert
 */
@SuppressWarnings("all")
@Component
public class TransformConvertFactory {

    private final Map<Class<?>, TransformTypeConvert> map = new ConcurrentHashMap<>();

    public <T> void addConvert(TransformTypeConvert<T> transformTypeConvert) {
        Type genericInterface = transformTypeConvert.getClass().getGenericInterfaces()[0];
        ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
        ParameterizedType actualTypeArgument = (ParameterizedType)parameterizedType.getActualTypeArguments()[0];
        map.put((Class<?>) actualTypeArgument.getRawType(), transformTypeConvert);
    }

    public <T> TransformTypeConvert<T> getTranslateTypeConvert(Class<T> convertClass) {
        return map.get(convertClass);
    }
}
