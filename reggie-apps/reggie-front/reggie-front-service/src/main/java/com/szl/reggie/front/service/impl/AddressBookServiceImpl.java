package com.szl.reggie.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.szl.reggie.entity.AddressBook;
import com.szl.reggie.front.mapper.AddressBookMapper;
import com.szl.reggie.front.service.AddressBookService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService(interfaceClass = AddressBookService.class)
public class AddressBookServiceImpl implements AddressBookService {
    @Autowired
    private AddressBookMapper addressBookMapper;

    @Override
    public void save(AddressBook addressBook) {
        addressBookMapper.insert(addressBook);
    }

    @Override
    public void updateByUserId(AddressBook addressBook, String currentId) {
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId, currentId);
        wrapper.set(AddressBook::getIsDefault, 0);
        //SQL:update address_book set is_default = 0 where user_id = ?
        addressBookMapper.update(addressBook, wrapper);
    }

    @Override
    public void updateById(AddressBook addressBook) {
        addressBookMapper.updateById(addressBook);
    }

    @Override
    public AddressBook getById(Long id) {
        return addressBookMapper.selectById(id);
    }

    @Override
    public AddressBook getByCondition(AddressBook addressBook) {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.eq(AddressBook::getIsDefault, addressBook.getIsDefault());
        //SQL:select * from address_book where user_id = ? and is_default = 1
        return addressBookMapper.selectOne(queryWrapper);
    }

    @Override
    public List<AddressBook> list(AddressBook addressBook) {
        //条件构造器
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null != addressBook.getUserId(), AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);
        return addressBookMapper.selectList(queryWrapper);
    }

}
