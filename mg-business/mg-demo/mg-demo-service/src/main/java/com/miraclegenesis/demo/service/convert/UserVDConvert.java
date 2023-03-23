package com.miraclegenesis.demo.service.convert;

import com.miraclegenesis.demo.api.model.UserVO;
import com.miraclegenesis.demo.model.dto.UserDTO;
import com.miraclegenesis.framework.common.convert.BaseConvert;
import org.mapstruct.Mapper;

/**
 * @Author robert
 */
@Mapper(componentModel = "spring")
public interface UserVDConvert extends BaseConvert<UserDTO, UserVO> {

}
