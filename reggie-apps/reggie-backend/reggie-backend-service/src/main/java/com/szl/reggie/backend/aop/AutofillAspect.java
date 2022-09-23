package com.szl.reggie.backend.aop;

import com.szl.reggie.common.constant.CacheKey;
import lombok.extern.slf4j.Slf4j;
import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Aspect
@Component
@Slf4j
public class AutofillAspect {
    @Autowired
    private CacheChannel cacheChannel;

    //切入点：待增强的方法
    @Pointcut("execution(public * com.szl.reggie.handler.DefaultMetaObjectHandler.*(..))")
    //切入点签名
    public void log() {
        System.out.println("pointCut签名。。。");
    }

    //前置通知
    @Before("log()")
    public void deBefore(JoinPoint joinPoint) throws Throwable {
        log.info("执行aop");
        CacheObject cacheObject = cacheChannel.get(CacheKey.LOGIN, "employee");
        Object target = joinPoint.getTarget();
        Long employeeId = (Long) cacheObject.getValue();
        Field empId = joinPoint.getTarget().getClass().getField("empId");
        empId.set(target,employeeId);
    }

}
