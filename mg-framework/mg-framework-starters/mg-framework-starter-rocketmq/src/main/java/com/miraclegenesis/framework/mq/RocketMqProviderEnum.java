package com.miraclegenesis.framework.mq;

import com.miraclegenesis.framework.common.model.IDict;

/**
 * @author robert
 */
public enum RocketMqProviderEnum implements IDict<String> {

    //
    OPEN_SOURCE("open_source", "开源rocketmq"),
    ALI_CLOUD("ali_cloud", "阿里云rocketmq");

    private final String code;
    private final String text;

    RocketMqProviderEnum(String code, String text) {
        this.code = code;
        this.text = text;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getText() {
        return text;
    }
}
