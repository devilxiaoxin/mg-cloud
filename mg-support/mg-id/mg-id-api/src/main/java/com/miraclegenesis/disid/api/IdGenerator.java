package com.miraclegenesis.disid.api;


import com.miraclegenesis.disid.api.segment.BusinessDisIdSegmentChain;

/**
 * @author robert
 */
public interface IdGenerator {

    /**
     * 获取整型id
     * @param businessId 业务标识，唯一
     * @return 主键id
     */
    Long getLongDisId(String businessId);


    /**
     * 先预获取号段链 ，调用号段链getId 方法性能最佳
     * @param businessId 业务id
     * @return
     */
    BusinessDisIdSegmentChain getBusinessDisIdSegmentChain(String businessId);
}
