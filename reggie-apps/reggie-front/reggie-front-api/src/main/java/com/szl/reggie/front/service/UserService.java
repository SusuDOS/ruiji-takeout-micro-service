package com.szl.reggie.front.service;

import com.szl.reggie.dto.UserDto;
public interface UserService {
    String getCurrentId();

    UserDto getUserByCondition(UserDto userDto);

    Long save(UserDto user);

}
