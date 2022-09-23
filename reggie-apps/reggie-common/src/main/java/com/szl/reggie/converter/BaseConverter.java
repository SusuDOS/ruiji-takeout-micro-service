package com.szl.reggie.converter;

import java.util.List;

public interface BaseConverter<T,R> {
    T dto2Entity(R r);

    List<T> dto2Entity4List(List<R> listR);

    R entity2Dto(T t);

    List<R> entity2Dto4List(List<T> t);


}
