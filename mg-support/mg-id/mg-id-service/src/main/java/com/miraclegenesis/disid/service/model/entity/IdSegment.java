package com.miraclegenesis.disid.service.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.miraclegenesis.framework.mysql.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author robert
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("id_segment")
public class IdSegment extends BaseEntity {

    @TableId
    private Long id;
    private String businessId;
    private Long autoIncrement;
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;
}
