package com.szl.reggie.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szl.reggie.backend.converter.CategoryConverter;
import com.szl.reggie.backend.mapper.CategoryMapper;
import com.szl.reggie.backend.mapper.DishMapper;
import com.szl.reggie.backend.mapper.SetmealMapper;
import com.szl.reggie.backend.service.CategoryService;
import com.szl.reggie.dto.CategoryDto;
import com.szl.reggie.entity.Category;
import com.szl.reggie.entity.Dish;
import com.szl.reggie.entity.Setmeal;
import com.szl.reggie.exception.CustomException;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService(interfaceClass = CategoryService.class)
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public boolean save(CategoryDto categoryDto) {
        Category category = CategoryConverter.INSTANCE.dto2Entity(categoryDto);
        return categoryMapper.insert(category) > 0;
    }

    @Override
    public Page<Category> page(int page, int pageSize) {
        //分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件，根据sort进行排序
        queryWrapper.orderByAsc(Category::getSort);

        //分页查询
        categoryMapper.selectPage(pageInfo, queryWrapper);
        return pageInfo;
    }

    @Override
    public boolean remove(Long id) {
        //添加查询条件，根据分类id进行查询菜品数据
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        long count1 = dishMapper.selectCount(dishLambdaQueryWrapper);
        //如果已经关联，抛出一个业务异常
        if (count1 > 0) {
            throw new CustomException("当前分类下关联了菜品，不能删除");//已经关联菜品，抛出一个业务异常
        }

        //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        long count2 = setmealMapper.selectCount(setmealLambdaQueryWrapper);
        if (count2 > 0) {
            throw new CustomException("当前分类下关联了套餐，不能删除");//已经关联套餐，抛出一个业务异常
        }

        //正常删除分类
        return categoryMapper.deleteById(id) > 0;
    }

    @Override
    public Boolean updateById(CategoryDto categoryDto) {
        Category category = CategoryConverter.INSTANCE.dto2Entity(categoryDto);
        return categoryMapper.updateById(category) > 0;
    }

    @Override
    public List<CategoryDto> list(CategoryDto categoryDto) {
        Category category = CategoryConverter.INSTANCE.dto2Entity(categoryDto);
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        return CategoryConverter.INSTANCE.entity2Dto4List(categories);
    }
}
