package com.szl.reggie.front.service;

import com.szl.reggie.dto.ShoppingCartDto;

import java.util.List;

public interface ShoppingCartService {
    ShoppingCartDto add(ShoppingCartDto shoppingCartDto);

    List<ShoppingCartDto> list();

    Boolean remove();

    ShoppingCartDto sub(ShoppingCartDto shoppingCartDto);
}
