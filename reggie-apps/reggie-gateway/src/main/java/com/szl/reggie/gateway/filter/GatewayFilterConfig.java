package com.szl.reggie.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.szl.reggie.auth.client.utils.JwtTokenClientUtils;
import com.szl.reggie.auth.utils.JwtEmployeeInfo;
import com.szl.reggie.auth.utils.JwtUserInfo;
import com.szl.reggie.common.constant.CacheKey;

import lombok.extern.slf4j.Slf4j;
import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Order(2)
public class GatewayFilterConfig extends BaseFilter {
    @Autowired
    private CacheChannel cacheChannel;
    @Autowired
    private JwtTokenClientUtils jwtTokenClientUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        // 1、获取本次请求的URI
        String requestURI = request.getPath().toString();// /backend/index.html
        log.info("拦截到请求：{}", requestURI);

        // 2、判断本次请求是否需要处理
        boolean check = isIgnoreToken(request);

        // 3、如果不需要处理，则直接放行
        if (check) {
            log.info("本次请求{}不需要处理", requestURI);
            return chain.filter(exchange);
        }

        // 4、判断登录状态，如果已登录，则直接放行

        Long employeeId = null;
        CacheObject employeeToken = cacheChannel.get(CacheKey.LOGIN, "employeeToken");
        if (employeeToken.getValue() != null) {
            String strToken = employeeToken.getValue().toString();
            JwtEmployeeInfo employeeInfo = jwtTokenClientUtils.getEmployeeInfo(strToken);
            employeeId = employeeInfo.getId();

            if (employeeId != null) {
                log.info("用户已登录，用户id为：{}", employeeId);
                return chain.filter(exchange);
            }
        }

        Long userId = null;
        CacheObject userToken = cacheChannel.get(CacheKey.LOGIN, "userToken");
        if (userToken.getValue() != null) {
            String strToken = userToken.getValue().toString();
            JwtUserInfo userInfo = jwtTokenClientUtils.getUserInfo(strToken);
            userId = userInfo.getId();

            if (userId != null) {
                log.info("用户已登录，用户id为：{}", userId);
                return chain.filter(exchange);
            }
        }

        // 5、如果未登录则返回未登录结果，通过输出流方式向客户端页面响应数据
        log.info("用户未登录");
        return super.errorResponse("NOTLOGIN", HttpStatus.OK, response);
    }

}
