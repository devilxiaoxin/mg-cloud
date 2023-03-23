package com.miraclegenesis.disid.api.segment;

import com.miraclegenesis.framework.common.exception.BaseResultCodeEnum;
import com.miraclegenesis.framework.common.exception.BizException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

/**
 * @author robert
 */
@Slf4j
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
public class BusinessDisIdSegmentChain extends LinkedList<BusinessDisIdSegmentChain.BusinessDisIdGenerator> {


    private String businessId;

    /**
     * 安全距离
     */
    private volatile long safeDistance;

    private volatile long safeDistanceMappingId = 0;


    private volatile boolean doAdditionalSegmentIsRunning = false;


    private final transient Object lock = new Object();

    /**
     * 号段最后一个id
     */
    private volatile long lastId = 0;

    /**
     * 上次预取数量
     */
    private Long lastPrefetchingCount;

    /**
     * 上次预取时间（往里面塞号段的时间 时间）
     */
    private LocalDateTime putSegmentChainTime;

    /**
     * 饥饿时间
     */
    private LocalDateTime hungerTime;

    /**
     * 剩余可用
     */
    private AtomicLong availableCount;


    /**
     * 追加号段
     */
    private transient Function<BusinessDisIdSegmentChain, BusinessDisIdGenerator> additionalSegment;

    public BusinessDisIdSegmentChain(String businessId, Long safeDistance, Long lastPrefetchingCount, Function<BusinessDisIdSegmentChain, BusinessDisIdGenerator> additionalSegment) {
        this.businessId = businessId;
        this.safeDistance = safeDistance;
        this.lastPrefetchingCount = lastPrefetchingCount;
        this.additionalSegment = additionalSegment;
        this.availableCount = new AtomicLong(0);
        doAdditionalSegment(1);
    }

    @Getter
    @Setter
    public static class BusinessDisIdGenerator {
        private String businessId;

        private Long startId;

        private Long endId;

        private AtomicLong atomicLong;

        private volatile Boolean available;

        public BusinessDisIdGenerator(String businessId, Long startId, Long endId) {
            this.businessId = businessId;
            this.startId = startId;
            this.endId = endId;
            this.atomicLong = new AtomicLong(startId);
            this.available = true;
        }

        public Long getId() {
            long nextId = this.atomicLong.getAndIncrement();
            if (endId > nextId) {
                return nextId;
            } else {
                this.available = false;
                return null;
            }
        }

        public Long availableCount() {
            if (Boolean.FALSE.equals(available)) {
                return 0L;
            }
            return endId - atomicLong.get();
        }

        public Long length() {
            return endId - startId;
        }
    }

    private ReentrantLock reentrantLock = new ReentrantLock(true);


    /**
     * 只需要单线程触发一次，下一个线程将不会进来
     * <p>
     * 还可能会有一个问题， 当一个线程获取下一个号段为空的时候，
     * 这个时候 上一个线程释放了锁了，这个时候进来是多余的，锁内应该再判断一次 是否需要追加号段
     * 所以应该要双重验证
     */
    private void doAdditionalSegment(long id) {
        if (id == lastId) {
            synchronized (lock) {
                while (id == lastId && doAdditionalSegmentIsRunning) {
                    try {
                        lock.wait(2000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        log.error("获取id 等待超时", e);
                        throw new BizException(BaseResultCodeEnum.SYSTEM_ERROR);
                    }
                }
            }
        }

        //没有在运行获取号段的线程  并且已经触发获取id的阈值
        if ((!doAdditionalSegmentIsRunning && id >= safeDistanceMappingId)) {
            boolean tryLock = reentrantLock.tryLock();
            //上锁并且二次校验
            try {
                if (tryLock && !doAdditionalSegmentIsRunning && id >= safeDistanceMappingId) {
                    //双层校验
                    hungerTime = LocalDateTime.now();
                    doAdditionalSegmentIsRunning = true;
                    try {
                        CompletableFuture.runAsync(() -> {
                            BusinessDisIdGenerator newBusinessDisIdGenerator = additionalSegment.apply(this);
                            setLastNext(newBusinessDisIdGenerator);
                            doAdditionalSegmentIsRunning = false;
                            synchronized (lock) {
                                lock.notifyAll();
                            }
                        });
                    } catch (Exception e) {
                        log.error("追加号段失败", e);
                    }
                }
            } finally {
                reentrantLock.unlock();
            }

        }
    }

    public Long getId() {
        BusinessDisIdGenerator businessDisIdGenerator = peekFirst();
        if (Objects.isNull(businessDisIdGenerator)) {
            doAdditionalSegment(lastId);
            return getId();
        }
        Long id = businessDisIdGenerator.getId();
        if (Objects.isNull(id)) {
            BusinessDisIdGenerator nextBusinessDisIdGenerator = getNext();
            if (Objects.isNull(nextBusinessDisIdGenerator)) {
                doAdditionalSegment(lastId);
                return getId();
            } else {
                return nextBusinessDisIdGenerator.getId();
            }
        }
        //触发 追加号段
        doAdditionalSegment(id);
        return id;
    }

    private BusinessDisIdGenerator getNext() {
        synchronized (lock) {
            BusinessDisIdGenerator businessDisIdGenerator = peekFirst();
            if (businessDisIdGenerator == null || businessDisIdGenerator.getAvailable()) {
                return businessDisIdGenerator;
            }
            //当前的已经不可用了，删除掉然后返回下一个号段
            poll();
            return peekFirst();
        }
    }

    void setLastNext(BusinessDisIdGenerator nextBusinessDisIdGenerator) {
        putSegmentChainTime = LocalDateTime.now();
        availableCount.addAndGet(nextBusinessDisIdGenerator.getEndId() - nextBusinessDisIdGenerator.getStartId());
        offer(nextBusinessDisIdGenerator);
        List<BusinessDisIdGenerator> businessDisIdGeneratorList = new ArrayList<>(this);
        Collections.reverse(businessDisIdGeneratorList);

        //循环从最后一个开始 往前面算，根据当前的安全距离 往前面推算，触发安全阈值的id 是哪一个
        long safeDistanceLimit = this.safeDistance;
        for (BusinessDisIdGenerator businessDisIdGenerator : businessDisIdGeneratorList) {
            safeDistanceLimit = safeDistanceLimit - businessDisIdGenerator.length();
            if (safeDistanceLimit < 0) {
                this.safeDistanceMappingId = businessDisIdGenerator.getEndId() + safeDistanceLimit;
                return;
            }
        }
        if (peek() != null) {
            this.safeDistanceMappingId = peek().getStartId();
        } else {
            this.safeDistanceMappingId = 0;
        }
    }
}
