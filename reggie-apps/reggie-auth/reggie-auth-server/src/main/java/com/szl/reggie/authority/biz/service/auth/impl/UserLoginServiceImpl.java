package com.szl.reggie.authority.biz.service.auth.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.szl.reggie.auth.server.utils.JwtTokenServerUtils;
import com.szl.reggie.auth.utils.JwtUserInfo;
import com.szl.reggie.auth.utils.Token;
import com.szl.reggie.authority.biz.service.auth.UserLoginService;
import com.szl.reggie.base.R;
import com.szl.reggie.common.constant.CacheKey;
import com.szl.reggie.dto.UserDto;
import com.szl.reggie.entity.User;
import com.szl.reggie.front.service.UserService;
import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLoginServiceImpl implements UserLoginService {
    @Autowired
    private CacheChannel cacheChannel;
    @DubboReference
    private UserService userService;
    @Autowired
    private JwtTokenServerUtils jwtTokenServerUtils;
    @Override
    public R<UserDto> login(UserDto userDto) {
        //获取手机号
        String phone = userDto.getPhone();
        //获取验证码
        String code = userDto.getCode();
        //从缓存中获取保存的验证码
        CacheObject cacheObject = cacheChannel.get(CacheKey.CAPTCHA, phone);
        String codeInCache = cacheObject.getValue().toString();

        //进行验证码的比对（页面提交的验证码和Session中保存的验证码比对）
        if(codeInCache != null && codeInCache.equals(code)){
            //如果能够比对成功，说明登录成功
            userDto = userService.getUserByCondition(userDto);

            if(userDto == null){
                //判断当前手机号对应的用户是否为新用户，如果是新用户就自动完成注册
                User user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userDto = new UserDto();
                BeanUtils.copyProperties(user,userDto);
                Long userId = userService.save(userDto);
                userDto.setId(userId);
            }
            //将用户信息保存到缓存中
            cacheChannel.set(CacheKey.LOGIN,"user",userDto.getId());
            // 生成Jwt令牌，保存到缓存中
            Token token = generateUserToken(userDto);
            String strToken = token.getToken();
            cacheChannel.set(CacheKey.LOGIN,"userToken",strToken);

            return R.success(userDto);
        }
        return R.error("登录失败");
    }

    /**
     * 生成token
     * @param user
     * @return
     */
    private Token generateUserToken(User user) {
        JwtUserInfo jwtUserInfo= new JwtUserInfo(
                user.getId(),
                user.getName(),
                user.getPhone(),
                user.getSex(),
                user.getIdNumber(),
                user.getAvatar(),
                user.getStatus()
        );
        return jwtTokenServerUtils.generateUserToken(jwtUserInfo, null);
    }
}
