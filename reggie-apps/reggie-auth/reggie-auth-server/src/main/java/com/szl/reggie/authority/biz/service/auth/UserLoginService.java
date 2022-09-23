package com.szl.reggie.authority.biz.service.auth;

import com.szl.reggie.base.R;
import com.szl.reggie.dto.EmployeeDto;
import com.szl.reggie.dto.UserDto;

public interface UserLoginService {
    R<UserDto> login(UserDto userDto);
}
