package com.miraclegenesis.disid.service.manager;

import com.miraclegenesis.disid.service.model.entity.IdSegment;
import com.miraclegenesis.disid.service.service.DisIdSegmentService;
import com.miraclegenesis.disid.api.model.SegmentIdDO;
import com.miraclegenesis.framework.web.result.BizAssert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author robert
 */
@Component
public class DisIdManager {

    @Resource
    private DisIdSegmentService disIdSegmentService;

    @Transactional(rollbackFor = Exception.class)
    public SegmentIdDO getSegment(String businessId, Long segmentCount) {
        IdSegment idSegment = initBusinessSegment(businessId);
        Boolean result = disIdSegmentService.getBaseMapper().updateDisIdSegmentAutoIncrement(idSegment.getId(), segmentCount);
        BizAssert.isTrue(result, "获取号段异常");
        idSegment = disIdSegmentService.getById(idSegment.getId());
        return new SegmentIdDO()
                .setStartId(idSegment.getAutoIncrement() - segmentCount)
                .setEndId(idSegment.getAutoIncrement())
                .setBusinessId(businessId);
    }

    public IdSegment initBusinessSegment(String businessId) {
        return disIdSegmentService.saveOrUpdate(businessId);
    }
}
