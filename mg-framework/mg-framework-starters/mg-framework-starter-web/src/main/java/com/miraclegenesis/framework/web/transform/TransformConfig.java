package com.miraclegenesis.framework.web.transform;

import com.miraclegenesis.framework.web.transform.convert.PageTransformTypeConvert;
import com.miraclegenesis.framework.web.transform.convert.ResultWrapperTransformTypeConvert;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author robert
 */
@Configuration
public class TransformConfig {


    @Bean
    public AbstractTranslateConvertRegistry resultWrapperTranslateConvertRegistry() {
        return new AbstractTranslateConvertRegistry() {

            @Override
            public TransformTypeConvert<?> addTranslateTypeConvert() {
                return new ResultWrapperTransformTypeConvert();
            }
        };
    }

    @Bean
    public AbstractTranslateConvertRegistry pageTranslateConvertRegistry() {
        return new AbstractTranslateConvertRegistry() {

            @Override
            public TransformTypeConvert<?> addTranslateTypeConvert() {
                return new PageTransformTypeConvert();
            }
        };
    }
}
