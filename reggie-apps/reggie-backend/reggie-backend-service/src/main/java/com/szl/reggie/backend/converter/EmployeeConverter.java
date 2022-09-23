package com.szl.reggie.backend.converter;

import com.szl.reggie.dto.EmployeeDto;
import com.szl.reggie.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployeeConverter {
    EmployeeConverter INSTANCE = Mappers.getMapper(EmployeeConverter.class);

    Employee dto2Entity(EmployeeDto employeeDto);

    EmployeeDto entity2Dto(Employee employee);
}
