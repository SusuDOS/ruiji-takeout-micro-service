package com.szl.reggie.backend.converter;

import com.szl.reggie.converter.BaseConverter;
import com.szl.reggie.dto.SetmealDto;
import com.szl.reggie.entity.Setmeal;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SetmealConverter extends BaseConverter<Setmeal, SetmealDto> {
    SetmealConverter INSTANCE = Mappers.getMapper(SetmealConverter.class);
}
