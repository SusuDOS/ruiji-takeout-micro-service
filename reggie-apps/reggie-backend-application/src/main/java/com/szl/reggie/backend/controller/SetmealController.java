package com.szl.reggie.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szl.reggie.backend.service.SetmealService;
import com.szl.reggie.backend.utils.ReturnUtil;
import com.szl.reggie.base.R;
import com.szl.reggie.dto.DishDto;
import com.szl.reggie.dto.SetmealDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
@Slf4j
@Api(value = "SetmealController",tags = "套餐管理接口")
public class SetmealController {
    @DubboReference
    private SetmealService setmealService;

    @PostMapping
    @ApiOperation(value = "新增套餐")
    @ApiImplicitParam(name = "setmealDto",value = "套餐信息",required = true)
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("套餐信息：{}",setmealDto);
        return ReturnUtil.returnR(setmealService.saveWithDish(setmealDto),"新增套餐成功");
    }

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "套餐分页查询", tags = "套餐分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页展示条数", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "name", value = "查询条件", dataType = "String", paramType = "query", required = false)}
    )
    public R<Page<SetmealDto>> page(int page, int pageSize, String name){
        return ReturnUtil.returnR(setmealService.page(page,pageSize,name),"");
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "删除套餐")
    @ApiImplicitParam(name = "ids",value = "套餐id集合",required = true)
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids:{}",ids);
        return ReturnUtil.returnR(setmealService.removeWithDish(ids), "套餐删除成功");
    }


    /**
     * 根据条件查询套餐数据
     * @param setmealDto
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "根据条件查询套餐数据")
    @ApiImplicitParam(name = "setmealDto",value = "查询信息",required = true)
    public R<List<SetmealDto>> list(SetmealDto setmealDto){
        return ReturnUtil.returnR(setmealService.list(setmealDto),"");
    }
}
