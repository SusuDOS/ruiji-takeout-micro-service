package com.szl.reggie.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.szl.reggie.common.constant.CacheKey;
import com.szl.reggie.dto.UserDto;
import com.szl.reggie.entity.User;
import com.szl.reggie.front.converter.UserConverter;
import com.szl.reggie.front.mapper.UserMapper;
import com.szl.reggie.front.service.UserService;
import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CacheChannel cacheChannel;

    @Override
    public String getCurrentId() {
        CacheObject cacheObject = cacheChannel.get(CacheKey.LOGIN, "user");
        if(cacheObject.getValue() != null){
            return cacheObject.getValue().toString();
        }
        return null;
    }

    @Override
    public UserDto getUserByCondition(UserDto userDto) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        String phone = userDto.getPhone();
        if (phone != null) {
            queryWrapper.eq(User::getPhone, userDto.getPhone());
        }
        User user = userMapper.selectOne(queryWrapper);

        return UserConverter.INSTANCE.entity2Dto(user);
    }

    @Override
    public Long save(UserDto userDto) {
        User user = UserConverter.INSTANCE.dto2Entity(userDto);
        userMapper.insert(user);
        return user.getId();
    }

}
