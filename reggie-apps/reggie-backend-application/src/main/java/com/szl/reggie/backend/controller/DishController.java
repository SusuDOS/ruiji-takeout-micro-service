package com.szl.reggie.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szl.reggie.backend.service.DishService;
import com.szl.reggie.backend.utils.ReturnUtil;
import com.szl.reggie.base.R;
import com.szl.reggie.dto.DishDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/dish")
@Slf4j
@Api(value = "DishController",tags = "菜品管理接口")
public class DishController {
    @DubboReference
    private DishService dishService;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    @ApiOperation(value = "保存菜品")
    @ApiImplicitParam(name = "dishDto",value = "菜品信息",required = true)
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        return ReturnUtil.returnR(dishService.saveWithFlavor(dishDto),"新增菜品成功");
    }


    /**
     * 菜品信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "菜品分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "当前页码",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页条数",required = true),
            @ApiImplicitParam(name = "name",value = "查询条件",required = false)
    })
    public R<Page<DishDto>> page(int page, int pageSize, String name){
        return ReturnUtil.returnR(dishService.page(page,pageSize,name),"");
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询菜品信息")
    @ApiImplicitParam(name = "id",value = "菜品id",required = true)
    public R<DishDto> get(@PathVariable Long id){
        return ReturnUtil.returnR(dishService.getByIdWithFlavor(id),"");
    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改菜品信息")
    @ApiImplicitParam(name = "dishDto",value = "菜品信息",required = true)
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        return ReturnUtil.returnR(dishService.updateWithFlavor(dishDto),"修改菜品成功");
    }

    /**
     * 根据条件查询对应的菜品数据
     * @param dishDto
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "根据条件查询对应的菜品数据")
    @ApiImplicitParam(name = "dishDto",value = "查询条件",required = true)
    public R<List<DishDto>> list(DishDto dishDto){
        return ReturnUtil.returnR(dishService.list(dishDto),"");

    }
}
