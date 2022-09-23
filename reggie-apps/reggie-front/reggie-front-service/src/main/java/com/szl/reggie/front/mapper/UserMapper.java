package com.szl.reggie.front.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.szl.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
