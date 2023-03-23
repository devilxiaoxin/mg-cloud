package com.miraclegenesis.disid.api.segment;

import com.miraclegenesis.disid.api.IdGeneratorDubboService;
import com.miraclegenesis.disid.api.model.SegmentIdDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author robert
 */
@Slf4j
@Component
public class BusinessDisIdSegmentCache {
    /**
     * 业务号段map，号段采用链表形式往下面接
     */
    private final Map<String, BusinessDisIdSegmentChain> idGeneratorAutoIncrement = new ConcurrentHashMap<>();

    /**
     * 默认安全距离
     */
    private static final long DEFAULT_SAFE_DISTANCE = 5L;

    /**
     * 默认预取号段数量
     */
    private static final long DEFAULT_PREFETCHING_COUNT = 100;

    /**
     * 饥饿阈值，单位秒
     */
    private static final int HUNGER_THRESHOLD = 30;

    private static final long MAX_PREFETCH_DISTANCE = Long.MAX_VALUE;

    @DubboReference
    private IdGeneratorDubboService idGeneratorDubboService;

    /**
     * 初始化，并获取号段链
     *
     * @param businessId 业务标识
     * @return 号段链
     */
    public BusinessDisIdSegmentChain getBusinessDisIdGenerator(String businessId) {
        return idGeneratorAutoIncrement.computeIfAbsent(businessId, s ->
                new BusinessDisIdSegmentChain(businessId, DEFAULT_SAFE_DISTANCE, DEFAULT_PREFETCHING_COUNT, this::generatorSegmentChain));
    }

    /**
     * 生成号段链
     */
    public BusinessDisIdSegmentChain.BusinessDisIdGenerator generatorSegmentChain(BusinessDisIdSegmentChain businessDisIdSegmentChain) {

        String businessId = businessDisIdSegmentChain.getBusinessId();

        LocalDateTime putSegmentChainTime = businessDisIdSegmentChain.getPutSegmentChainTime();

        LocalDateTime hungerTime = businessDisIdSegmentChain.getHungerTime();

        long prefetchingCount;

        long wakeupTimeGap = Optional.ofNullable(putSegmentChainTime)
                .map(localDateTime -> Duration.between(putSegmentChainTime, hungerTime).toMillis() / 1000)
                .orElse((long) HUNGER_THRESHOLD);


        if (wakeupTimeGap < HUNGER_THRESHOLD) {
            log.info("饥饿了");
            prefetchingCount = Math.min(Math.multiplyExact(businessDisIdSegmentChain.getLastPrefetchingCount(), Math.max(Math.floorDiv(HUNGER_THRESHOLD, Math.max(wakeupTimeGap, 1)), 2)), MAX_PREFETCH_DISTANCE);
        } else {
            log.info("不饿");
            prefetchingCount = Math.max(Math.floorDiv(businessDisIdSegmentChain.getLastPrefetchingCount(), 2), businessDisIdSegmentChain.getSafeDistance());
        }


        log.info("本次预取号段 {}", prefetchingCount);
        businessDisIdSegmentChain.setLastPrefetchingCount(prefetchingCount);
        businessDisIdSegmentChain.setSafeDistance(Math.max(Math.floorDiv(prefetchingCount, 2), DEFAULT_SAFE_DISTANCE));

        SegmentIdDO segmentIdDO = idGeneratorDubboService.generateSegmentId(businessId, prefetchingCount);

        return new BusinessDisIdSegmentChain.BusinessDisIdGenerator(segmentIdDO.getBusinessId(), segmentIdDO.getStartId(), segmentIdDO.getEndId());
    }
}
