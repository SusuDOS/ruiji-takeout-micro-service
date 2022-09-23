package com.szl.reggie.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szl.reggie.backend.converter.DishConverter;
import com.szl.reggie.backend.mapper.CategoryMapper;
import com.szl.reggie.backend.mapper.DishFlavorMapper;
import com.szl.reggie.backend.mapper.DishMapper;
import com.szl.reggie.backend.service.DishFlavorService;
import com.szl.reggie.backend.service.DishService;
import com.szl.reggie.dto.DishDto;
import com.szl.reggie.entity.Category;
import com.szl.reggie.entity.Dish;
import com.szl.reggie.entity.DishFlavor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@DubboService(interfaceClass = DishService.class)
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Transactional
    public Boolean saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish  此处因为原项目Dto继承了Entity，为保持统一，不再使用mapstruct转换
        dishMapper.insert(dishDto);

        Long dishId = dishDto.getId();//菜品id
        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().peek((item) -> item.setDishId(dishId)).collect(Collectors.toList());
        //保存菜品口味数据到菜品口味表dish_flavor
        return dishFlavorService.saveBatch(flavors);
    }

    @Override
    public Page<DishDto> page(int page, int pageSize, String name) {
        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null, Dish::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //执行分页查询
        dishMapper.selectPage(pageInfo, queryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryMapper.selectById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return dishDtoPage;
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息，从dish表查询
        Dish dish = dishMapper.selectById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        //查询当前菜品对应的口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorMapper.selectList(queryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Override
    @Transactional
    public Boolean updateWithFlavor(DishDto dishDto) {
        try {
            //更新dish表基本信息
            dishMapper.updateById(dishDto);
            //清理当前菜品对应口味数据---dish_flavor表的delete操作
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<DishFlavor>();
            queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
            dishFlavorMapper.delete(queryWrapper);
            //添加当前提交过来的口味数据---dish_flavor表的insert操作
            List<DishFlavor> flavors = dishDto.getFlavors();
            flavors = flavors.stream().peek((item) -> item.setDishId(dishDto.getId())).collect(Collectors.toList());
            dishFlavorService.saveBatch(flavors);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public List<DishDto> list(DishDto dishDto) {
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dishDto.getCategoryId() != null, Dish::getCategoryId, dishDto.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus, 1);
        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishMapper.selectList(queryWrapper);

        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto thisDishDto = new DishDto();
            BeanUtils.copyProperties(item, thisDishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryMapper.selectById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                thisDishDto.setCategoryName(categoryName);
            }

            //当前菜品的id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);
            //SQL:select * from dish_flavor where dish_id = ?
            List<DishFlavor> dishFlavorList = dishFlavorMapper.selectList(lambdaQueryWrapper);
            thisDishDto.setFlavors(dishFlavorList);

            return thisDishDto;
        }).collect(Collectors.toList());

        return dishDtoList;
    }

}
