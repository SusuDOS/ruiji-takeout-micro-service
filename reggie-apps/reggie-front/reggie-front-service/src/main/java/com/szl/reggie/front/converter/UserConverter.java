package com.szl.reggie.front.converter;

import com.szl.reggie.converter.BaseConverter;
import com.szl.reggie.dto.UserDto;
import com.szl.reggie.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserConverter extends BaseConverter<User, UserDto> {
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);
}
