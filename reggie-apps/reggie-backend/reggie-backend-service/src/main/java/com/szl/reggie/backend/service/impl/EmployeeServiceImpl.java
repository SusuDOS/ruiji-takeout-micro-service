package com.szl.reggie.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szl.reggie.backend.converter.EmployeeConverter;
import com.szl.reggie.backend.mapper.EmployeeMapper;
import com.szl.reggie.backend.service.EmployeeService;
import com.szl.reggie.dto.EmployeeDto;
import com.szl.reggie.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import net.oschina.j2cache.CacheChannel;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

@DubboService(interfaceClass = EmployeeService.class)
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private CacheChannel cacheChannel;

    @Override
    public EmployeeDto getEmployeeByCondition(EmployeeDto employeeDto) {
        Employee employee = EmployeeConverter.INSTANCE.dto2Entity(employeeDto);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        String username = employee.getUsername();
        Long id = employee.getId();
        if(username!=null){
            queryWrapper.eq(Employee::getUsername, username);
        }
        if(id != null){
            queryWrapper.eq(Employee::getId, id);
        }

        Employee employeeEq = employeeMapper.selectOne(queryWrapper);
        return EmployeeConverter.INSTANCE.entity2Dto(employeeEq);
    }

    @Override
    public Boolean save(EmployeeDto employeeDto) {
        Employee employee = EmployeeConverter.INSTANCE.dto2Entity(employeeDto);
        //设置初始密码123456，需要进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

        //获得当前登录用户的id
//        CacheObject cacheObject = cacheChannel.get(CacheKey.LOGIN,"employee");
//        Long empId = (Long) cacheObject.getValue();
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);
        employeeMapper.insert(employee);
        return true;
    }

    @Override
    public Page<Employee> page(int page, int pageSize, String name) {
        //构造分页构造器
        Page<Employee> pageInfo = new Page<Employee>(page,pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<Employee>();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        employeeMapper.selectPage(pageInfo,queryWrapper);
        return pageInfo;
        //执行查询
    }

    @Override
    public Boolean updateEmployeeByCondition(EmployeeDto employeeDto) {
        Employee employee = EmployeeConverter.INSTANCE.dto2Entity(employeeDto);

        //获得当前登录用户的id
//        CacheObject cacheObject = cacheChannel.get(CacheKey.LOGIN,"employee");
//        Long empId = (Long) cacheObject.getValue();
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);

        int updateById = employeeMapper.updateById(employee);

        return updateById > 0;
    }
}
