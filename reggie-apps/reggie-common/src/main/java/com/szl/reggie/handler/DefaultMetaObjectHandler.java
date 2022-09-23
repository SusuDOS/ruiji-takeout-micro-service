package com.szl.reggie.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

public class DefaultMetaObjectHandler implements MetaObjectHandler {
    public Long empId;
    public Long userId;
    @Override
    public void insertFill(MetaObject metaObject) {
        Long currentId = null;
        if(empId != null){
            currentId = empId;
        }
        if(userId != null){
            currentId = userId;
        }
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("createUser", currentId);
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", currentId);

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Long currentId = null;
        if(empId != null){
            currentId = empId;
        }
        if(userId != null){
            currentId = userId;
        }
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", currentId);
    }
}
