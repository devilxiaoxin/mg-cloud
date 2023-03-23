package com.miraclegenesis.framework.mq.consumer.ali;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.miraclegenesis.framework.mq.consumer.ConsumerContainerBuilder;
import com.miraclegenesis.framework.mq.producer.AliMqProducerConfiguration;
import com.miraclegenesis.framework.common.mq.MqTopic;
import com.miraclegenesis.framework.mq.RocketMqProviderEnum;
import com.miraclegenesis.framework.mq.annotation.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

/**
 * @author robert
 */
@Slf4j
@Component
public class AliMqConsumerContainerBuilder extends ConsumerContainerBuilder {

    private final ObjectMapper objectMapper;

    private final AliMqProducerConfiguration aliMqProducerConfiguration;


    public AliMqConsumerContainerBuilder(AliMqProducerConfiguration aliMqProducerConfiguration, JavaTimeModule javaTimeModule) {
        this.aliMqProducerConfiguration = aliMqProducerConfiguration;
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(javaTimeModule);
    }


    @Override
    public void doRegisterContainer(Class<?> targetClass, String beanName, Object bean, String topic, String groupId, Consumer consumer) {

        String tag = consumer.tag();

        Properties properties = aliMqProducerConfiguration.initAliMqProperties();
        ConsumerBean consumerBean = new ConsumerBean();
        //配置文件
        properties.setProperty(PropertyKeyConst.GROUP_ID, groupId);
        //将消费者线程数固定为20个 20为默认值
        properties.setProperty(PropertyKeyConst.ConsumeThreadNums, "20");
        consumerBean.setProperties(properties);

        @SuppressWarnings("unchecked")
        MqTopic<Object> messageContext = (MqTopic<Object>) bean;

        ResolvableType resolvableType = ResolvableType.forClass(targetClass);
        ResolvableType generic = resolvableType.getInterfaces()[0].getInterfaces()[0].getGeneric(0);

        MessageListener messageListener = (message, context) -> {
            log.info("consumer {}", message);
            message.getBody();
            Object body;
            try {
                //反序列化 如果报错了，就直接跳过
                body = objectMapper.readValue(message.getBody(), generic.getRawClass());
            } catch (IOException e) {
                log.error("alimq 消费失败, {}", message, e);
                return Action.CommitMessage;
            }
            try {
                messageContext.consumer(body);
            } catch (Exception e) {
                return Action.ReconsumeLater;
            }

            return Action.CommitMessage;
        };
        //订阅关系
        Map<Subscription, MessageListener> subscriptionTable = new HashMap<>();
        Subscription subscription = new Subscription();
        subscription.setTopic(topic);
        subscription.setExpression(tag);
        subscriptionTable.put(subscription, messageListener);
        //订阅多个topic如上面设置
        consumerBean.setSubscriptionTable(subscriptionTable);

        String containerBeanName = String.format("%s_%s", DefaultRocketMQListenerContainer.class.getName(), UUID.randomUUID());
        consumerBean.start();
        log.info("Register the listener to container, listenerBeanName:{}, containerBeanName:{}, topic: {}, groupId: {}", beanName, containerBeanName, topic, groupId);

    }

    @Override
    public RocketMqProviderEnum rocketMqProvider() {
        return RocketMqProviderEnum.ALI_CLOUD;
    }
}
