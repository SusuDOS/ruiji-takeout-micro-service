package com.szl.reggie.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szl.reggie.backend.service.EmployeeService;
import com.szl.reggie.base.R;
import com.szl.reggie.dto.EmployeeDto;
import com.szl.reggie.entity.Employee;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/employee")
@Slf4j
@Api(value = "EmployeeController", tags = "员工管理接口")
public class EmployeeController {
    @DubboReference(/*timeout = 2000,mock = "true"*/)
    private EmployeeService employeeService;

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
     * 新增员工
     *
     * @param employeeDto
     * @return
     */
    @PostMapping
    @ApiOperation(value = "添加员工", notes = "添加员工")
    @ApiImplicitParam(name = "employeeDto", value = "员工信息", required = true, dataType = "EmployeeDto", paramType = "body")
    public R<String> save(@RequestBody EmployeeDto employeeDto) {
        log.info("新增员工，员工信息：{}", employeeDto.toString());

        return returnR(employeeService.save(employeeDto), "新增员工成功");
    }

    /**
     * 员工信息分页查询
     *
     * @param page     当前查询页码
     * @param pageSize 每页展示记录数
     * @param name     员工姓名 - 可选参数
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "员工分页查询", tags = "员工分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页展示条数", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "name", value = "查询条件", dataType = "String", paramType = "query", required = false)}
    )
    public R<Page<Employee>> page(int page, int pageSize, String name) {
        log.info("page = {},pageSize = {},name = {}", page, pageSize, name);

        //执行查询
        return returnR(employeeService.page(page, pageSize, name), "");
    }

    /**
     * 根据id修改员工信息
     *
     * @param employeeDto
     * @return
     */
    @PutMapping
    @ApiOperation(value = "更新员工信息", tags = "更新员工信息")
    @ApiImplicitParam(name = "employeeDto", value = "员工信息", dataType = "employeeDto", paramType = "body", required = true)
    public R<String> update(@RequestBody EmployeeDto employeeDto) {
        log.info(employeeDto.toString());

        return returnR(employeeService.updateEmployeeByCondition(employeeDto), "新增员工成功");

    }

    /**
     * 根据id查询员工信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询员工信息", notes = "根据id查询员工信息")
    @ApiImplicitParam(name = "id", value = "员工id", dataType = "Long", paramType = "path",required = true)
    public R<EmployeeDto> getById(@PathVariable Long id) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(id);
        log.info("根据id查询员工信息...");
        return returnR(employeeService.getEmployeeByCondition(employeeDto), "");
    }
}
