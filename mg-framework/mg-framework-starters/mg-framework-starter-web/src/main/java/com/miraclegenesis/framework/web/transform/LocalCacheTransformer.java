package com.miraclegenesis.framework.web.transform;

import com.miraclegenesis.framework.common.cache.AbstractLocalCache;
import com.miraclegenesis.framework.common.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Optional;

/**
 * @author robert
 */
@Component
@Slf4j
public class LocalCacheTransformer<K, V> implements Transformer<K> {

    @SuppressWarnings("unchecked")
    @Override
    public String transform(K original, Class<?> datasource, String param) {
        if (AbstractLocalCache.class.isAssignableFrom(datasource)) {
            AbstractLocalCache<K, V> abstractLocalCache = (AbstractLocalCache<K, V>) SpringContextUtil.getBean(datasource);
            V cache = abstractLocalCache.getCache(original);
            Field field;
            try {
                if (cache == null) {
                    log.error("翻译无内容 cacheId {} datasource {}", original, datasource);
                    return null;
                }
                field = cache.getClass().getDeclaredField(param);
            } catch (NoSuchFieldException e) {
                log.error("找不到字段 {}", param, e);
                return null;
            }
            try {
                field.setAccessible(true);
                Object o = field.get(cache);
                Optional<Object> optional = Optional.ofNullable(o);
                return optional.map(Object::toString).orElse(null);
            } catch (IllegalAccessException e) {
                log.error("获取 class {} 字段 {} 失败", cache.getClass(), param);
                return null;
            }
        }
        return null;
    }
}
