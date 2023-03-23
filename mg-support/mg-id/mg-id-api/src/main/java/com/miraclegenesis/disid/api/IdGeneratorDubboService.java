package com.miraclegenesis.disid.api;


import com.miraclegenesis.disid.api.model.SegmentIdDO;

/**
 * @author robert
 */
public interface IdGeneratorDubboService {

    /**
     * 生成号段
     * @return 号段实体
     */
    SegmentIdDO generateSegmentId(String businessId, Long segmentCount);
}
