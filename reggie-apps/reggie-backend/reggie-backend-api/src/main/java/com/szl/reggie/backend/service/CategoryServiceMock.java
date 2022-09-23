package com.szl.reggie.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szl.reggie.dto.CategoryDto;
import com.szl.reggie.entity.Category;

import java.util.List;

public class CategoryServiceMock implements CategoryService{
    @Override
    public boolean save(CategoryDto categoryDto) {
        return false;
    }

    @Override
    public Page<Category> page(int page, int pageSize) {
        return null;
    }

    @Override
    public boolean remove(Long id) {
        return false;
    }

    @Override
    public Boolean updateById(CategoryDto categoryDto) {
        return null;
    }

    @Override
    public List<CategoryDto> list(CategoryDto categoryDto) {
        return null;
    }
}
