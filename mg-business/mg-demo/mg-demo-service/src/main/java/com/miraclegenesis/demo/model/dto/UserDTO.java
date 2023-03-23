package com.miraclegenesis.demo.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author robert
 */
@Data
public class UserDTO implements Serializable {

    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private String address;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

}
