package com.miraclegenesis.disid.api.segment;

import com.miraclegenesis.disid.api.IdGenerator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author robert
 */
@Service
public class SegmentIdGenerator implements IdGenerator {

    @Resource
    private BusinessDisIdSegmentCache businessDisIdSegment;

    @Override
    public Long getLongDisId(String businessId) {
        BusinessDisIdSegmentChain businessDisIdSegmentChain = businessDisIdSegment.getBusinessDisIdGenerator(businessId);
        return businessDisIdSegmentChain.getId();
    }

    @Override
    public BusinessDisIdSegmentChain getBusinessDisIdSegmentChain(String businessId) {
        return businessDisIdSegment.getBusinessDisIdGenerator(businessId);
    }
}
