package com.szl.reggie.front.service.impl;

import com.szl.reggie.entity.OrderDetail;
import com.szl.reggie.front.mapper.OrderDetailMapper;
import com.szl.reggie.front.service.OrderDetailService;
import com.szl.reggie.front.service.OrdersService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService(interfaceClass = OrderDetailService.class)
public class OrderDetailServiceImpl implements OrderDetailService {
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Override
    public void saveBatch(List<OrderDetail> orderDetails) {
        for (OrderDetail orderDetail : orderDetails) {
            orderDetailMapper.insert(orderDetail);
        }
    }
}
