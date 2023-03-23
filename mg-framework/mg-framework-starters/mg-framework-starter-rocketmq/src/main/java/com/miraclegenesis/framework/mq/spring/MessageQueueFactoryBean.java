package com.miraclegenesis.framework.mq.spring;

import com.miraclegenesis.framework.mq.producer.MessageQueueInvocationHandler;
import org.springframework.beans.factory.FactoryBean;


/**
 * @author robert
 */
public class MessageQueueFactoryBean<T> implements FactoryBean<T> {

    public final Class<T> type;

    public MessageQueueFactoryBean(Class<T> type) {
        this.type = type;
    }


    @SuppressWarnings("unchecked")
    @Override
    public T getObject() {
        return (T) MessageQueueInvocationHandler.generateProxyClass(type);
    }

    @Override
    public Class<?> getObjectType() {
        return type;
    }
}
