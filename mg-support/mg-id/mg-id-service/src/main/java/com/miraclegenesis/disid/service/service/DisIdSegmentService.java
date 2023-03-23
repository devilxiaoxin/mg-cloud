package com.miraclegenesis.disid.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miraclegenesis.disid.service.model.constant.RedisKeysConstants;
import com.miraclegenesis.disid.service.mapper.DisIdSegmentMapper;
import com.miraclegenesis.disid.service.model.entity.IdSegment;
import com.miraclegenesis.framework.redis.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author robert
 */
@Slf4j
@Service
public class DisIdSegmentService extends ServiceImpl<DisIdSegmentMapper, IdSegment> {

    @Resource
    private RedisClient redisClient;

    public IdSegment saveOrUpdate(String businessId) {
        IdSegment idSegment = this.lambdaQuery().eq(IdSegment::getBusinessId, businessId).one();
        if (idSegment != null) {
            return idSegment;
        }
        RLock lock = redisClient.getLock(RedisKeysConstants.INIT_BUSINESS_DIS_ID_LOCK.concat(businessId));
        try {
            lock.tryLock(20, 20, TimeUnit.MINUTES);
            idSegment = this.lambdaQuery().eq(IdSegment::getBusinessId, businessId).one();
            if (Objects.isNull(idSegment)) {
                idSegment = new IdSegment()
                        .setBusinessId(businessId)
                        .setAutoIncrement(1L);
                this.save(idSegment);
            }
            return idSegment;
        } catch (Exception e) {
            log.error("获取号段失败", e);
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
        return null;
    }


}
