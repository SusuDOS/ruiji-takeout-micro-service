//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.szl.reggie.backend.filter;

import java.lang.reflect.Method;

import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.common.utils.ReflectUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.Filter.Listener;
import org.apache.dubbo.rpc.filter.ExceptionFilter;
import org.apache.dubbo.rpc.service.GenericService;

@Activate(
        group = {"provider"}
)
public class DubboExceptionFilter implements Filter, Listener {
    private Logger logger = LoggerFactory.getLogger(ExceptionFilter.class);

    public DubboExceptionFilter() {
    }

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        return invoker.invoke(invocation);
    }

    public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
        if (appResponse.hasException() && GenericService.class != invoker.getInterface()) {
            try {
                Throwable exception = appResponse.getException();
                if (!(exception instanceof RuntimeException) && exception instanceof Exception) {
                    return;
                }

                try {
                    Method method = invoker.getInterface().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
                    Class<?>[] exceptionClassses = method.getExceptionTypes();
                    Class[] var7 = exceptionClassses;
                    int var8 = exceptionClassses.length;

                    for (int var9 = 0; var9 < var8; ++var9) {
                        Class<?> exceptionClass = var7[var9];
                        if (exception.getClass().equals(exceptionClass)) {
                            return;
                        }
                    }
                } catch (NoSuchMethodException var11) {
                    return;
                }

                this.logger.error("Got unchecked and undeclared exception which called by " + RpcContext.getContext().getRemoteHost() + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName() + ", exception: " + exception.getClass().getName() + ": " + exception.getMessage(), exception);
                String serviceFile = ReflectUtils.getCodeBase(invoker.getInterface());
                String exceptionFile = ReflectUtils.getCodeBase(exception.getClass());
                if (serviceFile != null && exceptionFile != null && !serviceFile.equals(exceptionFile)) {
                    String className = exception.getClass().getName();
                    if (!className.startsWith("java.") && !className.startsWith("javax.")) {
                        if (className.startsWith("com.szl")) {
                            return;
                        }
                        if (className.startsWith("org.springframework")) {
                            return;
                        }
                        if (exception instanceof RpcException) {
                            return;
                        }

                        appResponse.setException(new RuntimeException(StringUtils.toString(exception)));
                        return;
                    }

                    return;
                }

                return;
            } catch (Throwable var12) {
                this.logger.warn("Fail to ExceptionFilter when called by " + RpcContext.getContext().getRemoteHost() + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName() + ", exception: " + var12.getClass().getName() + ": " + var12.getMessage(), var12);
            }
        }

    }

    public void onError(Throwable e, Invoker<?> invoker, Invocation invocation) {
        this.logger.error("Got unchecked and undeclared exception which called by " + RpcContext.getContext().getRemoteHost() + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName() + ", exception: " + e.getClass().getName() + ": " + e.getMessage(), e);
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
