package com.miraclegenesis.demo.api.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author robert
 */
@Data
public class UserVO implements Serializable {

    private Long id;

    private String name;

    private String address;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;
}
