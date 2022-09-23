package com.szl.reggie.front.service;

import com.szl.reggie.dto.OrdersDto;

public interface OrdersService {
    Boolean submit(OrdersDto ordersDto);
}
