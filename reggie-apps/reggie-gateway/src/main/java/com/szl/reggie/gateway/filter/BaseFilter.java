package com.szl.reggie.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.szl.reggie.base.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public abstract class BaseFilter implements GlobalFilter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    /**
     * 判断当前请求uri是否需要忽略
     */
    protected boolean isIgnoreToken(ServerHttpRequest request) {
        //1、获取本次请求的URI
        String requestURI = request.getPath().toString();// /backend/index.html
        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login",
                "/error",
        };

        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }

    /**
     * 网关抛异常
     * @param errMsg
     * @param httpStatus
     * @param response
     * @return
     */
    protected Mono<Void> errorResponse(String errMsg, HttpStatus httpStatus, ServerHttpResponse response) {
        R<Object> error = R.error(errMsg);
        // 返回错误码
        response.setStatusCode(httpStatus);
        //通过输出流方式向客户端页面响应数据
        String jsonString = JSON.toJSONString(error);
        response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        DataBuffer buffer = response.bufferFactory().wrap(jsonString.getBytes());
        return response.writeWith(Flux.just(buffer));
    }
}
