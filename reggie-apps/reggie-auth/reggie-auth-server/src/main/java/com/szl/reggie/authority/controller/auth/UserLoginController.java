package com.szl.reggie.authority.controller.auth;

import com.szl.reggie.authority.biz.service.auth.UserLoginService;
import com.szl.reggie.authority.utils.SMSUtils;
import com.szl.reggie.authority.utils.ValidateCodeUtils;
import com.szl.reggie.base.R;
import com.szl.reggie.common.constant.CacheKey;
import com.szl.reggie.dto.UserDto;
import com.szl.reggie.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.oschina.j2cache.CacheChannel;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Api(tags = "用户登录接口")
@Slf4j
public class UserLoginController {
    @Autowired
    private CacheChannel cacheChannel;
    @Autowired
    private UserLoginService userLoginService;

    /**
     * 发送手机短信验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    @ApiOperation(value = "发送手机短信验证码")
    @ApiImplicitParam(name = "user",value = "用户登录信息",required = true)
    public R<String> sendMsg(@RequestBody User user){
        //获取手机号
        String phone = user.getPhone();
        if(StringUtils.isNotEmpty(phone)){
            //生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);

            //调用阿里云提供的短信服务API完成发送短信
            SMSUtils.sendMessage("瑞吉外卖","",phone,code);
            cacheChannel.set(CacheKey.CAPTCHA,phone,code);
            //需要将生成的验证码保存到缓存
            cacheChannel.set(CacheKey.CAPTCHA,phone,code);

            return R.success("手机验证码短信发送成功");
        }
        return R.error("短信发送失败");
    }


    /**
     * 移动端用户登录
     * @param userDto
     * @return
     */
    @PostMapping("/login")
    public R<UserDto> login(@RequestBody UserDto userDto){
        log.info(userDto.toString());
        return userLoginService.login(userDto);
    }
}
