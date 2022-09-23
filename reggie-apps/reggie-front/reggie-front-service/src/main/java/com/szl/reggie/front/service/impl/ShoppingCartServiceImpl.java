package com.szl.reggie.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.szl.reggie.base.R;
import com.szl.reggie.dto.ShoppingCartDto;
import com.szl.reggie.entity.ShoppingCart;
import com.szl.reggie.exception.CustomException;
import com.szl.reggie.front.converter.ShoppingChartConverter;
import com.szl.reggie.front.mapper.ShoppingCartMapper;
import com.szl.reggie.front.service.ShoppingCartService;
import com.szl.reggie.front.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@DubboService(interfaceClass = ShoppingCartService.class)
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @DubboReference
    private UserService userService;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    public ShoppingCartDto add(ShoppingCartDto shoppingCartDto) {
        ShoppingCart shoppingCart = ShoppingChartConverter.INSTANCE.dto2Entity(shoppingCartDto);
        //设置用户id，指定当前是哪个用户的购物车数据
        Long currentId = this.getCurrentUser();

        shoppingCart.setUserId(currentId);

        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);

        if (dishId != null) {
            //添加到购物车的是菜品
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            //添加到购物车的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        //查询当前菜品或者套餐是否在购物车中
        //SQL:select * from shopping_cart where user_id = ? and dish_id/setmeal_id = ?
        ShoppingCart cartServiceOne = shoppingCartMapper.selectOne(queryWrapper);

        if (cartServiceOne != null) {
            //如果已经存在，就在原来数量基础上加一
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number + 1);
            shoppingCartMapper.updateById(cartServiceOne);
        } else {
            //如果不存在，则添加到购物车，数量默认就是一
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
            cartServiceOne = shoppingCart;
        }
        return ShoppingChartConverter.INSTANCE.entity2Dto(cartServiceOne);
    }

    @Override
    public List<ShoppingCartDto> list() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, this.getCurrentUser());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartMapper.selectList(queryWrapper);

        return ShoppingChartConverter.INSTANCE.entity2Dto4List(list);
    }

    @Override
    public Boolean remove() {
        try {
            //SQL:delete from shopping_cart where user_id = ?
            LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ShoppingCart::getUserId,getCurrentUser());

            shoppingCartMapper.delete(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        }
        return true;
    }

    @Override
    public ShoppingCartDto sub(ShoppingCartDto shoppingCartDto) {
        ShoppingCart shoppingCart = ShoppingChartConverter.INSTANCE.dto2Entity(shoppingCartDto);

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        Long userId = getCurrentUser();
        shoppingCart.setUserId(userId);
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        if (shoppingCart.getDishId() != null) {
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        } else {
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());

        }

        ShoppingCart cart = shoppingCartMapper.selectOne(queryWrapper);
        cart.setNumber(cart.getNumber() - 1);
        if (cart.getNumber() == 0) {
            shoppingCartMapper.delete(queryWrapper);
            ShoppingChartConverter.INSTANCE.entity2Dto(cart);
        }
        shoppingCartMapper.updateById(cart);
        return ShoppingChartConverter.INSTANCE.entity2Dto(cart);
    }

    private Long getCurrentUser() {
        return Long.parseLong(userService.getCurrentId());
    }
}
