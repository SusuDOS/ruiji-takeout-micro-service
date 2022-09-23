package com.szl.reggie.front.controller;

import com.szl.reggie.base.R;
import com.szl.reggie.dto.OrdersDto;
import com.szl.reggie.front.service.OrdersService;
import com.szl.reggie.front.utils.ReturnUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/order")
@Api(tags = "订单功能接口")
public class OrderController {

    @DubboReference
    private OrdersService ordersService;

    /**
     * 用户下单
     * @param ordersDto
     * @return
     */
    @PostMapping("/submit")
    @ApiOperation(value = "用户下单")
    @ApiImplicitParam(name = "ordersDto",value = "订单信息",required = true)
    public R<String> submit(@RequestBody OrdersDto ordersDto){
        log.info("订单数据：{}",ordersDto);
        return ReturnUtil.returnR(ordersService.submit(ordersDto),"下单成功");
    }
}