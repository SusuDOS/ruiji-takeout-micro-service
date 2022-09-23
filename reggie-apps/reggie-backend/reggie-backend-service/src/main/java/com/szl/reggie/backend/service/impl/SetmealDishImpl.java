package com.szl.reggie.backend.service.impl;

import com.szl.reggie.backend.mapper.SetmealDishMapper;
import com.szl.reggie.backend.service.SetmealDishService;
import com.szl.reggie.entity.SetmealDish;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService(interfaceClass = SetmealDishService.class)
public class SetmealDishImpl implements SetmealDishService {
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Override
    public Boolean saveBatch(List<SetmealDish> setmealDishes) {
        try {
            for (SetmealDish setmealDish : setmealDishes) {
                setmealDishMapper.insert(setmealDish);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
