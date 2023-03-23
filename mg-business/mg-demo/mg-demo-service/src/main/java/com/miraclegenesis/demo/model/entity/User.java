package com.miraclegenesis.demo.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.miraclegenesis.framework.mysql.BaseEntity;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("user")
public class User extends BaseEntity implements Serializable {

    @TableId
    private Long id;

    private String name;

    private String address;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;
}
