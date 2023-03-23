package com.miraclegenesis.disid.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author robert
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class SegmentIdDO implements Serializable {

    private String businessId;

    private Long startId;

    private Long endId;
}
