package com.szl.reggie.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szl.reggie.dto.CategoryDto;
import com.szl.reggie.entity.Category;

import java.util.List;

public interface CategoryService {
    boolean save(CategoryDto categoryDto);

    Page<Category> page(int page, int pageSize);
    boolean remove(Long id);

    Boolean updateById(CategoryDto categoryDto);

    List<CategoryDto> list(CategoryDto categoryDto);
}
