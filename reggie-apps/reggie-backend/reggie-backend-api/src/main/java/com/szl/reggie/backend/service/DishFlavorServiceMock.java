package com.szl.reggie.backend.service;

import com.szl.reggie.entity.DishFlavor;

import java.util.List;

public class DishFlavorServiceMock implements DishFlavorService{
    @Override
    public Boolean saveBatch(List<DishFlavor> flavors) {
        return null;
    }
}
