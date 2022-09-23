package com.szl.reggie.backend.converter;

import com.szl.reggie.converter.BaseConverter;
import com.szl.reggie.dto.CategoryDto;
import com.szl.reggie.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryConverter extends BaseConverter<Category, CategoryDto> {
    CategoryConverter INSTANCE = Mappers.getMapper(CategoryConverter.class);
}
