package com.szl.reggie.gateway.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.reactive.config.CorsRegistry;

/**
 * 根据版本决定是否添加此类
 */
@SpringBootConfiguration
public class GatewayConfiguration {
    public void addCorsMappings(CorsRegistry corsRegistry){
        /**
         * 所有请求都允许跨域，使用这种配置就不需要
         * 在interceptor中配置header了
         */
        corsRegistry.addMapping("/**")
                .allowCredentials(true)
                .allowedOrigins("http://localhost:8090")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}





