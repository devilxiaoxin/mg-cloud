package com.miraclegenesis.framework.web.transform;

import org.springframework.context.SmartLifecycle;

import javax.annotation.Resource;

/**
 * @author robert
 */
public abstract class AbstractTranslateConvertRegistry implements SmartLifecycle {

    @Resource
    private TransformConvertFactory transformConvertFactory;

    /**
     * 增加转换器
     * @return TransformTypeConvert
     */
    public abstract TransformTypeConvert<?> addTranslateTypeConvert();

    @Override
    public void start() {
        transformConvertFactory.addConvert(addTranslateTypeConvert());
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
