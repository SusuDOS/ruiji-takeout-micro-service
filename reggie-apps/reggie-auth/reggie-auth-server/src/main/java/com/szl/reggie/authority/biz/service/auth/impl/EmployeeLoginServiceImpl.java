package com.szl.reggie.authority.biz.service.auth.impl;

import com.szl.reggie.auth.server.utils.JwtTokenServerUtils;
import com.szl.reggie.auth.utils.JwtEmployeeInfo;
import com.szl.reggie.auth.utils.Token;
import com.szl.reggie.authority.biz.service.auth.EmployeeLoginService;
import com.szl.reggie.backend.service.EmployeeService;
import com.szl.reggie.base.R;
import com.szl.reggie.common.constant.CacheKey;
import com.szl.reggie.dto.EmployeeDto;
import lombok.extern.slf4j.Slf4j;
import net.oschina.j2cache.CacheChannel;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
@Slf4j
public class EmployeeLoginServiceImpl implements EmployeeLoginService {

    @DubboReference(/*timeout = 2000,retries = 2,mock = "true"*/)
    private EmployeeService employeeService;
    @Autowired
    private CacheChannel cacheChannel;
    @Autowired
    private JwtTokenServerUtils jwtTokenServerUtils;

    @Override
    public R<EmployeeDto> login(EmployeeDto employeeDto) {
        //1、将页面提交的密码password进行md5加密处理
        String password = employeeDto.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2、根据页面提交的用户名username查询数据库
        EmployeeDto emp = employeeService.getEmployeeByCondition(employeeDto);
        //3、如果没有查询到则返回登录失败结果
        if (emp == null) {
            return R.error("登录失败");
        }

        //4、密码比对，如果不一致则返回登录失败结果
        if (!emp.getPassword().equals(password)) {
            return R.error("登录失败");
        }

        //5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }


        //6、登录成功，将员工id存入redis并返回登录成功结果,仅为衔接瑞吉教程内容，可忽略。
        cacheChannel.set(CacheKey.LOGIN, "employee", emp.getId());
        //7、 生成Jwt令牌，保存到缓存中
        Token token = generateEmployeeToken(emp);
        String strToken = token.getToken();
        cacheChannel.set(CacheKey.LOGIN,"employeeToken",strToken);
        // TODO: 2022/5/29 查询当前用户权限，保存到缓存中

        return R.success(emp);
    }

    /**
     * 生成token
     * @param employeeDto
     * @return
     */
    private Token generateEmployeeToken(EmployeeDto employeeDto) {
        JwtEmployeeInfo jwtEmployeeInfo = new JwtEmployeeInfo(
                employeeDto.getId(),
                employeeDto.getUsername(),
                employeeDto.getName(),
                employeeDto.getPassword(),
                employeeDto.getPhone(),
                employeeDto.getSex(),
                employeeDto.getIdNumber(),
                employeeDto.getStatus()
        );
        return jwtTokenServerUtils.generateEmployeeToken(jwtEmployeeInfo, null);
    }
}
