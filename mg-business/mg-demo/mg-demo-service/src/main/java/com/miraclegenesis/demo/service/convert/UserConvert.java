package com.miraclegenesis.demo.service.convert;

import com.miraclegenesis.demo.model.dto.UserDTO;
import com.miraclegenesis.demo.model.entity.User;
import com.miraclegenesis.framework.common.convert.BaseConvert;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserConvert extends BaseConvert<User, UserDTO> {


}
