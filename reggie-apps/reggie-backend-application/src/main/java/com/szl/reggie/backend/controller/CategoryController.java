package com.szl.reggie.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szl.reggie.backend.service.CategoryService;
import com.szl.reggie.base.R;
import com.szl.reggie.dto.CategoryDto;
import com.szl.reggie.entity.Category;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 */
@RestController
@RequestMapping("/category")
@Slf4j
@Api(value = "CategoryController", tags = "分类管理接口")
public class CategoryController {
    @DubboReference
    private CategoryService categoryService;

    /**
     * 返回处理
     *
     * @param
     * @param msg
     * @return
     */
    private <T, O> R<T> returnR(O o, String msg) {
        if (o == null) {
            return R.error("服务异常");
        }
        if (o instanceof Boolean) {
            Boolean result = (Boolean) o;
            if (!result) {
                return R.error("服务异常");
            }

            return R.success((T) msg);
        }

        return R.success((T) o);
    }

    /**
     * 新增分类
     *
     * @param category
     * @return
     */
    @PostMapping
    @ApiOperation(value = "添加分类", notes = "添加分类")
    @ApiImplicitParam(name = "category", value = "分类信息", dataType = "CategoryDto", paramType = "body", required = true)
    public R<String> save(@RequestBody CategoryDto category) {
        log.info("category:{}", category);
        return returnR(categoryService.save(category), "添加分类成功");
    }

    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "分类功能分页查询", notes = "分类功能分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", dataType = "int", paramType = "query", required = true)
    })
    public R<Page<Category>> page(int page, int pageSize) {
        return returnR(categoryService.page(page, pageSize), "");
    }

    /**
     * 根据id删除分类
     *
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "删除分类", notes = "删除分类")
    @ApiImplicitParam(name = "id", value = "分类id", dataType = "Long", paramType = "query", required = true)
    public R<String> delete(Long id) {
        log.info("删除分类，id为：{}", id);

        //categoryService.removeById(id);
        return returnR(categoryService.remove(id), "分类信息删除成功");
    }

    /**
     * 根据id修改分类信息
     *
     * @param categoryDto
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改分类信息", notes = "修改分类信息")
    @ApiImplicitParam(name = "category", value = "分类信息", dataType = "CategoryDto", paramType = "body", required = true)
    public R<String> update(@RequestBody CategoryDto categoryDto) {
        log.info("修改分类信息：{}", categoryDto);
        return returnR(categoryService.updateById(categoryDto), "分类信息修改成功");
    }


    /**
     * 根据条件查询分类数据
     *
     * @param categoryDto
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "根据条件查询分类数据", notes = "根据条件查询分类数据")
    @ApiImplicitParam(name = "categoryDto", value = "查询条件", dataType = "CategoryDto", paramType = "body", required = true)
    public R<List<CategoryDto>> list(CategoryDto categoryDto) {
        return returnR(categoryService.list(categoryDto), "");
    }
}
