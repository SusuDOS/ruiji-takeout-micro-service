package com.szl.reggie.backend.converter;

import com.szl.reggie.converter.BaseConverter;
import com.szl.reggie.dto.DishDto;
import com.szl.reggie.entity.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DishConverter extends BaseConverter<Dish, DishDto> {
    DishConverter INSTANCE = Mappers.getMapper(DishConverter.class);
}
