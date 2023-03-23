package com.miraclegenesis.framework.web;

import com.miraclegenesis.framework.web.exception.GlobalExceptionHandler;
import com.miraclegenesis.framework.web.exception.SentinelBlockExceptionHandler;
import com.miraclegenesis.framework.web.result.InitializingAdviceDecorator;
import com.miraclegenesis.framework.web.swagger.SwaggerConfiguration;
import com.miraclegenesis.framework.web.swagger.SwaggerShortcutController;
import com.miraclegenesis.framework.web.transform.TransformConfig;
import com.miraclegenesis.framework.web.config.DateTimeConfig;
import com.miraclegenesis.framework.web.config.DateTimeConvertPrimaryConfig;
import com.miraclegenesis.framework.web.config.ValidatorConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author robert
 */
@Configuration
@ComponentScan(basePackages = "com.miraclegenesis.framework.web")
@Import({SwaggerConfiguration.class, InitializingAdviceDecorator.class,
        GlobalExceptionHandler.class, SentinelBlockExceptionHandler.class,
        DateTimeConfig.class, ValidatorConfig.class, SwaggerShortcutController.class, TransformConfig.class, DateTimeConvertPrimaryConfig.class})
public class WebAutoConfig {

}
