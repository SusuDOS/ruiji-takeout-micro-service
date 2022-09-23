package com.szl.reggie.front.converter;

import com.szl.reggie.converter.BaseConverter;
import com.szl.reggie.dto.ShoppingCartDto;
import com.szl.reggie.entity.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ShoppingChartConverter extends BaseConverter<ShoppingCart, ShoppingCartDto> {
    ShoppingChartConverter INSTANCE = Mappers.getMapper(ShoppingChartConverter.class);
}
