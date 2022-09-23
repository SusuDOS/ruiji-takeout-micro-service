package com.szl.reggie.front.service;

import com.szl.reggie.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    void save(AddressBook addressBook);
    
    void updateByUserId(AddressBook addressBook, String currentId);

    void updateById(AddressBook addressBook);

    AddressBook getById(Long id);

    AddressBook getByCondition(AddressBook addressBook);

    List<AddressBook> list(AddressBook addressBook);
}
