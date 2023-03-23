package com.miraclegenesis.framework.mq.consumer;

import com.miraclegenesis.framework.common.utils.SpringContextUtil;
import com.miraclegenesis.framework.mq.RocketMqProviderEnum;
import com.miraclegenesis.framework.mq.annotation.Consumer;

/**
 * @author robert
 */
public abstract class ConsumerContainerBuilder {

    /**
     * 注册容器
     *
     * @param targetClass 目标类
     * @param beanName    bean名称
     * @param bean        消费者的bean
     * @param topic       topic名字
     * @param groupId     消费组id
     * @param consumer    消费者配置
     */
    public abstract void doRegisterContainer(Class<?> targetClass, String beanName, Object bean, String topic, String groupId, Consumer consumer);

    /**
     * mq 服务提供者
     *
     * @return 服务提供者
     */
    public abstract RocketMqProviderEnum rocketMqProvider();

    public void registerContainer(Class<?> targetClass, String beanName, Object bean, String topic, Consumer consumer) {
        String groupId = SpringContextUtil.getSpringValue(consumer.groupId());
        doRegisterContainer(targetClass, beanName, bean, topic, groupId, consumer);
    }
}
