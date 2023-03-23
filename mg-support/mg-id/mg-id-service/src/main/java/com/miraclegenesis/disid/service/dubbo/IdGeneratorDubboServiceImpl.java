package com.miraclegenesis.disid.service.dubbo;

import com.miraclegenesis.disid.service.manager.DisIdManager;
import com.miraclegenesis.disid.api.IdGeneratorDubboService;
import com.miraclegenesis.disid.api.model.SegmentIdDO;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;


/**
 * @author robert
 */
@DubboService
public class IdGeneratorDubboServiceImpl implements IdGeneratorDubboService {

    @Resource
    private DisIdManager disIdManager;

    @Override
    public SegmentIdDO generateSegmentId(String businessId, Long segmentCount) {
        return disIdManager.getSegment(businessId, segmentCount);
    }
}
