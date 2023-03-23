package com.miraclegenesis.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miraclegenesis.demo.mapper.UserMapper;
import com.miraclegenesis.demo.model.dto.UserDTO;
import com.miraclegenesis.demo.model.entity.User;
import com.miraclegenesis.demo.service.UserService;
import com.miraclegenesis.demo.service.convert.UserConvert;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author robert
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserConvert userConvert;


    @Override
    public UserDTO getUserById(Long id) {
        return userConvert.toT(super.getById(id));
    }

    @Override
    public Boolean save(UserDTO userDTO) {
        return super.save(userConvert.toS(userDTO));
    }
}
