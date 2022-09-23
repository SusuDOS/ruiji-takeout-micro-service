package com.szl.reggie.front.service;

import com.szl.reggie.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    void saveBatch(List<OrderDetail> orderDetails);
}
