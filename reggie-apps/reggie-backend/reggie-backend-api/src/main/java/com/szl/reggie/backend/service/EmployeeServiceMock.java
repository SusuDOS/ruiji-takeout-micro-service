package com.szl.reggie.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szl.reggie.dto.EmployeeDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmployeeServiceMock implements EmployeeService{
    @Override
    public EmployeeDto getEmployeeByCondition(EmployeeDto employeeDto) {
        String simpleClassName = this.getClass().getSimpleName();
        String serviceName = simpleClassName.split("Mock")[0];
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.warn("======================{}服务异常，{}方法触发服务降级=======================",serviceName,methodName);
        return null;
    }

    @Override
    public Boolean save(EmployeeDto employeeDto) {
        String simpleClassName = this.getClass().getSimpleName();
        String serviceName = simpleClassName.split("Mock")[0];
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.warn("======================{}服务异常，{}方法触发服务降级=======================",serviceName,methodName);
        return false;
    }

    @Override
    public Page page(int page, int pageSize, String name) {
        return null;
    }

    @Override
    public Boolean updateEmployeeByCondition(EmployeeDto employeeDto) {
        return null;
    }
}
