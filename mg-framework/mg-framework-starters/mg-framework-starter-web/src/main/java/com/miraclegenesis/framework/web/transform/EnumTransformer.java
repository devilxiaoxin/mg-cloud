package com.miraclegenesis.framework.web.transform;

import com.miraclegenesis.framework.common.model.IDict;

import java.util.stream.Stream;

/**
 * @author robert
 */
public class EnumTransformer implements Transformer<Object> {

    @Override
    @SuppressWarnings("unchecked")
    public String transform(Object original, Class<?> datasource, String param) {
        Class<IDict<?>> iDict = (Class<IDict<?>>) datasource;
        return Stream.of(iDict.getEnumConstants())
                .filter(i -> i.getCode().equals(original)).map(IDict::getText).findFirst().orElse(null);
    }
}
