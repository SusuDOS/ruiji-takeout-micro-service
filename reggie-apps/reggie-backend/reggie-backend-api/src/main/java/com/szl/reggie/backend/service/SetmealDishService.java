package com.szl.reggie.backend.service;

import com.szl.reggie.entity.SetmealDish;

import java.util.List;

public interface SetmealDishService {
    Boolean saveBatch(List<SetmealDish> setmealDishes);
}
