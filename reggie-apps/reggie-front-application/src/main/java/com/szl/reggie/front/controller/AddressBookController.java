package com.szl.reggie.front.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.szl.reggie.base.R;
import com.szl.reggie.entity.AddressBook;
import com.szl.reggie.front.service.AddressBookService;
import com.szl.reggie.front.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地址簿管理
 */
@Slf4j
@RestController
@RequestMapping("/addressBook")
@Api(tags = "地址簿管理")
public class AddressBookController {

    @DubboReference
    private AddressBookService addressBookService;
    @DubboReference
    private UserService userService;

    /**
     * 新增
     */
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook) {
        String currentId = userService.getCurrentId();
        if(currentId != null){
            addressBook.setUserId(Long.parseLong(currentId));
            log.info("addressBook:{}", addressBook);
            addressBookService.save(addressBook);
            return R.success(addressBook);
        }
      return R.error("用户未登录");
    }

    /**
     * 设置默认地址
     */
    @PutMapping("default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        log.info("addressBook:{}", addressBook);
        String currentId = userService.getCurrentId();

        addressBookService.updateByUserId(addressBook,currentId);

        addressBook.setIsDefault(1);
        //SQL:update address_book set is_default = 1 where id = ?
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }

    /**
     * 根据id查询地址
     */
    @GetMapping("/{id}")
    public R get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return R.success(addressBook);
        } else {
            return R.error("没有找到该对象");
        }
    }

    /**
     * 查询默认地址
     */
    @GetMapping("default")
    public R<AddressBook> getDefault() {
        String currentId = userService.getCurrentId();

        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(Long.parseLong(currentId));
        addressBook.setIsDefault(1);

        addressBook = addressBookService.getByCondition(addressBook);

        if (null == addressBook) {
            return R.error("没有找到该对象");
        } else {
            return R.success(addressBook);
        }
    }

    /**
     * 查询指定用户的全部地址
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {
        String currentId = userService.getCurrentId();
        addressBook.setUserId(Long.parseLong(currentId));
        log.info("addressBook:{}", addressBook);

        //条件构造器
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null != addressBook.getUserId(), AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        //SQL:select * from address_book where user_id = ? order by update_time desc
        return R.success(addressBookService.list(addressBook));
    }
}
