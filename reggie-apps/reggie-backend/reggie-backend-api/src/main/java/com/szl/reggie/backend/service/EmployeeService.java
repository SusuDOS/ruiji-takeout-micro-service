package com.szl.reggie.backend.service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szl.reggie.dto.EmployeeDto;
import com.szl.reggie.entity.Employee;

public interface EmployeeService{
        EmployeeDto getEmployeeByCondition(EmployeeDto employeeDto);
        Boolean save(EmployeeDto employeeDto);
        Page<Employee> page(int page, int pageSize, String name);
        Boolean updateEmployeeByCondition(EmployeeDto employeeDto);
}
