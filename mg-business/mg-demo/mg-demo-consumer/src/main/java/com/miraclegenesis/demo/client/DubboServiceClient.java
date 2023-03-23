package com.miraclegenesis.demo.client;

import com.miraclegenesis.demo.api.model.UserVO;
import com.miraclegenesis.demo.api.service.DemoDubboService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @Author robert
 */
@Slf4j
@Component
public class DubboServiceClient {

    @DubboReference
    private DemoDubboService demoDubboService;

    public UserVO getById(Long id) {
        return demoDubboService.getById(id);
    }
}
