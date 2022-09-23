package com.szl.reggie.authority.controller.auth;

import com.szl.reggie.authority.biz.service.auth.EmployeeLoginService;
import com.szl.reggie.base.R;
import com.szl.reggie.common.constant.CacheKey;
import com.szl.reggie.dto.EmployeeDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import net.oschina.j2cache.CacheChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
@Api(value = "EmployeeLoginController", tags = "后台系统员工登录")
public class EmployeeLoginController {
    @Autowired
    private CacheChannel cacheChannel;
    @Autowired
    private EmployeeLoginService employeeLoginService;

    @PostMapping("login")
    @ApiOperation(value = "用户登录功能", notes = "用户登录功能")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "EmployeeDto", value = "登录信息",
                    required = true, type = "EmployeeDto")
    })
    public R<EmployeeDto> login(@RequestBody EmployeeDto employeeDto){
      return employeeLoginService.login(employeeDto);
    }

    @ApiOperation(value = "用户退出功能")
    @PostMapping("/logout")
    public R<String> logout(){
//        redisTemplate.delete("employee");
        cacheChannel.evict(CacheKey.LOGIN,"employee");
        cacheChannel.evict(CacheKey.LOGIN,"employeeToken");
        return R.success("退出成功");
    }

}
