package com.szl.reggie.front.converter;


import com.szl.reggie.converter.BaseConverter;
import com.szl.reggie.dto.OrdersDto;
import com.szl.reggie.entity.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrdersConverter extends BaseConverter<Orders, OrdersDto> {
    OrdersConverter INSTANCE = Mappers.getMapper(OrdersConverter.class);
}
