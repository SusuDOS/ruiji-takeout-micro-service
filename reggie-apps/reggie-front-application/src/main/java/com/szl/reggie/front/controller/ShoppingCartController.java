package com.szl.reggie.front.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.szl.reggie.base.R;
import com.szl.reggie.dto.ShoppingCartDto;
import com.szl.reggie.entity.ShoppingCart;
import com.szl.reggie.front.service.ShoppingCartService;
import com.szl.reggie.front.service.UserService;
import com.szl.reggie.front.utils.ReturnUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
@Api(tags = "购物车功能接口")
public class ShoppingCartController {
    @DubboReference
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     *
     * @param shoppingCartDto
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加购物车")
    @ApiImplicitParam(name = "shoppingCartDto", value = "购物车信息", required = true)
    public R<ShoppingCartDto> add(@RequestBody ShoppingCartDto shoppingCartDto) {
        log.info("购物车数据:{}", shoppingCartDto);
        return ReturnUtil.returnR(shoppingCartService.add(shoppingCartDto), "");
    }

    /**
     * 查看购物车
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "查看购物车")
    public R<List<ShoppingCartDto>> list() {
        log.info("查看购物车...");
        return ReturnUtil.returnR(shoppingCartService.list(), "");
    }


    /**
     * 清空购物车
     *
     * @return
     */
    @DeleteMapping("/clean")
    @ApiOperation(value = "清空购物车")
    public R<String> clean() {

        return ReturnUtil.returnR(shoppingCartService.remove(), "");
    }


    @PostMapping("/sub")
    @ApiOperation(value = "购物车移除商品")
    @ApiImplicitParam(name = "shoppingCartDto", value = "购物车信息", required = true)
    public R<ShoppingCartDto> sub(@RequestBody ShoppingCartDto shoppingCartDto) {
        return ReturnUtil.returnR(shoppingCartService.sub(shoppingCartDto),"");
    }
}
