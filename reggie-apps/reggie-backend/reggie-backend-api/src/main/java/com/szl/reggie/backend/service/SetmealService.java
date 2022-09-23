package com.szl.reggie.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szl.reggie.dto.DishDto;
import com.szl.reggie.dto.SetmealDto;

import java.util.List;

public interface SetmealService {

    Boolean saveWithDish(SetmealDto setmealDto);

    Page<SetmealDto> page(int page, int pageSize, String name);

    Boolean removeWithDish(List<Long> ids);

    List<SetmealDto> list(SetmealDto setmealDto);
}
