package com.miraclegenesis.demo.dubbo;

import com.miraclegenesis.demo.api.model.UserVO;
import com.miraclegenesis.demo.api.service.DemoDubboService;
import com.miraclegenesis.demo.service.UserService;
import com.miraclegenesis.demo.service.convert.UserVDConvert;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @Author robert
 */
@DubboService
public class DemoDubboServiceImpl implements DemoDubboService {

    @Resource
    private UserService userService;

    @Resource
    private UserVDConvert userVDConvert;

    @Override
    public UserVO getById(Long id) {
        return userVDConvert.toT(userService.getUserById(id));
    }
}
