package com.szl.reggie.authority.biz.service.auth;

import com.szl.reggie.base.R;
import com.szl.reggie.dto.EmployeeDto;

public interface EmployeeLoginService {
    R<EmployeeDto> login(EmployeeDto employeeDto);
}
