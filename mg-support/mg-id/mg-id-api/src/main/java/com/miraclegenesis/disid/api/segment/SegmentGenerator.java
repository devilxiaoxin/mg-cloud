package com.miraclegenesis.disid.api.segment;


import com.miraclegenesis.disid.api.IdGeneratorDubboService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @author robert
 */
@Component
public class SegmentGenerator {

    @DubboReference
    private IdGeneratorDubboService idGeneratorDubboService;



}
