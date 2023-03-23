package com.miraclegenesis.demo.api.service;

import com.miraclegenesis.demo.api.model.UserVO;

/**
 * @Author robert
 */
public interface DemoDubboService {

    UserVO getById(Long id);
}
