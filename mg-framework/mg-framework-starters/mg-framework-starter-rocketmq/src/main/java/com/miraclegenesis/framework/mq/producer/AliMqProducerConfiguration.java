package com.miraclegenesis.framework.mq.producer;

import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.order.OrderProducer;
import com.miraclegenesis.framework.mq.config.RocketMqConfigurationProperties;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Properties;

/**
 * @author robert
 */
@Configuration
public class AliMqProducerConfiguration {
    @Resource
    private RocketMqConfigurationProperties rocketMqConfiguration;

    @Resource
    private RocketMQProperties rocketMqProperties;

    public Properties initAliMqProperties() {
        RocketMqConfigurationProperties.AliMqConfiguration aliMqConfiguration = rocketMqConfiguration.getAliMq();
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.AccessKey, aliMqConfiguration.getAccessKey());
        properties.setProperty(PropertyKeyConst.SecretKey, aliMqConfiguration.getSecretKey());
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, rocketMqProperties.getNameServer());
        return properties;
    }

//    @Bean(initMethod = "start", destroyMethod = "shutdown")
//    public ProducerBean buildProducer() {
//        ProducerBean producer = new ProducerBean();
//        producer.setProperties(initAliMqProperties());
//        return producer;
//    }
//
//
//    @Bean(initMethod = "start", destroyMethod = "shutdown")
//    public OrderProducerBean buildOrderProducer() {
//        OrderProducerBean orderProducerBean = new OrderProducerBean();
//        orderProducerBean.setProperties(initAliMqProperties());
//        return orderProducerBean;
//    }

    @Bean
    public Producer producer() {
        Properties properties = initAliMqProperties();
        Producer producer = ONSFactory.createProducer(properties);
        producer.start();
        return producer;
    }

    @Bean
    public OrderProducer orderProducer() {
        Properties properties = initAliMqProperties();
        OrderProducer orderProducer = ONSFactory.createOrderProducer(properties);
        orderProducer.start();
        return orderProducer;
    }
}
