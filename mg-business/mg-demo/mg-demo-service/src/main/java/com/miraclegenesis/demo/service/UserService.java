package com.miraclegenesis.demo.service;

import com.miraclegenesis.demo.model.dto.UserDTO;

/**
 * @author robert
 */
public interface UserService {

    /**
     *
     * @param id
     * @return
     */
    UserDTO getUserById(Long id);

    /**
     *
     * @param userDTO
     * @return
     */
    Boolean save(UserDTO userDTO);

}
