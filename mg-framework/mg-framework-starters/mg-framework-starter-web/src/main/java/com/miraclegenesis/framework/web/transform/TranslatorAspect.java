package com.miraclegenesis.framework.web.transform;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.miraclegenesis.framework.common.cache.LocalCache;
import com.miraclegenesis.framework.common.model.IDict;
import com.miraclegenesis.framework.common.utils.ReflectUtil;
import com.miraclegenesis.framework.common.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author robert
 */

@Aspect
@Component
@Order(2)
@Slf4j
public class TranslatorAspect {

    @Resource
    private TransformConvertFactory transformConvertFactory;

    @AfterReturning(pointcut = "@annotation(com.miraclegenesis.framework.web.transform.Transform)", returning = "object")
    @SuppressWarnings("unchecked")
    public void doAfter(JoinPoint joinPoint, Object object) {
        TransformTypeConvert<Object> transformTypeConvert = transformConvertFactory.getTranslateTypeConvert((Class<Object>) object.getClass());
        Object result;
        if (transformTypeConvert != null) {
            result = transformTypeConvert.convert(object);
        } else {
            result = object;
        }
        if (result instanceof Collection<?> || result instanceof IPage) {
            Collection<?> collection = result instanceof IPage ? ((IPage<?>) result).getRecords() : (Collection<?>) result;
            if (collection.isEmpty()) {
                return;
            }
            collection.forEach((Consumer<Object>) this::translateObject);
            log.info("返回列表结果 {}", collection);
        } else {
            this.translateObject(object);
            log.info("返回对象结果 {}", object);
        }
    }

    private void translateObject(Object result) {
        if (Objects.isNull(result)) {
            return;
        }
        Field[] fields = ReflectUtil.getFields(result.getClass());
        // 翻译当前对象字段
        Arrays.stream(fields).filter(field -> field.getAnnotation(Transform.class) != null)
                .forEach(field -> transform(result, field));
        // 翻译内部属性字段
        Arrays.stream(fields).filter(field -> field.getAnnotation(Transform.class) != null)
                .forEach(field -> {
                    try {
                        Object subObject = ReflectUtil.invokeGet(result, field);
                        if (subObject instanceof Collection<?>) {
                            ((Collection<?>) subObject).forEach(this::translateObject);
                        } else {
                            this.translateObject(subObject);
                        }
                    } catch (Exception e) {
                        log.error("翻译错误", e);
                    }
                });
    }

    @SuppressWarnings("unchecked")
    private void transform(Object vo, Field field) {
        try {
            Transform annotation = field.getAnnotation(Transform.class);
            Class<?> aClass = annotation.dataSource();
            Object fieldValue = ReflectUtil.invokeGet(vo, annotation.from());
            if (Objects.isNull(fieldValue)) {
                return;
            }
            if (IDict.class.isAssignableFrom(aClass)) {
                String translate = new EnumTransformer().transform(fieldValue, aClass, null);
                if (translate == null) {
                    return;
                }
                ReflectUtil.invokeSet(vo, field, translate);
            } else if (LocalCache.class.isAssignableFrom(annotation.dataSource())) {
                LocalCacheTransformer<Object, Object> localCacheTransformer = SpringContextUtil.getBean(LocalCacheTransformer.class);
                String translate = localCacheTransformer.transform(fieldValue, annotation.dataSource(), annotation.param());
                if (translate == null) {
                    return;
                }
                ReflectUtil.invokeSet(vo, field, translate);
            } else {
                Transformer<Object> transformer = SpringContextUtil.getBean(annotation.transformer());
                String translate = transformer.transform(fieldValue, annotation.dataSource(), annotation.param());
                if (translate == null) {
                    return;
                }
                ReflectUtil.invokeSet(vo, field, translate);
            }
        } catch (Exception e) {
            log.error("翻译错误", e);
        }
    }

}
