package com.szl.reggie.backend.service;

import com.szl.reggie.entity.DishFlavor;

import java.util.List;

public interface DishFlavorService {
    Boolean saveBatch(List<DishFlavor> flavors);
}
