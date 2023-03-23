package com.miraclegenesis.demo.controller;

import com.miraclegenesis.demo.api.model.UserVO;
import com.miraclegenesis.demo.client.DubboServiceClient;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author robert
 */
@Slf4j
@Api(tags = "Demo样例")
@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private DubboServiceClient dubboServiceClient;

    @GetMapping(value = "/user/{id}")
    public UserVO getUserById(@PathVariable Long id) {
        return dubboServiceClient.getById(id);
    }

}
