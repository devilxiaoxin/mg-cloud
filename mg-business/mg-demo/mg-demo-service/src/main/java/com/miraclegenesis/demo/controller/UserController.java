package com.miraclegenesis.demo.controller;


import com.miraclegenesis.demo.model.dto.UserDTO;
import com.miraclegenesis.demo.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author robert
 */

@Slf4j
@Api(tags = "Demo样例")
@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserService userService;


    @GetMapping(value = "/user/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }


    @PostMapping
    public Boolean save(@RequestBody UserDTO userDTO) {
        return userService.save(userDTO);
    }

}
