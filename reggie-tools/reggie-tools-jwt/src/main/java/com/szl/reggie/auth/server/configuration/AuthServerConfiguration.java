package com.szl.reggie.auth.server.configuration;


import com.szl.reggie.auth.server.properties.AuthServerProperties;
import com.szl.reggie.auth.server.utils.JwtTokenServerUtils;

import com.szl.reggie.auth.server.properties.AuthServerProperties;
import com.szl.reggie.auth.server.utils.JwtTokenServerUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 认证服务端配置
 *
 */
@EnableConfigurationProperties(value = {
        AuthServerProperties.class,
})
public class AuthServerConfiguration {
    @Bean
    public JwtTokenServerUtils getJwtTokenServerUtils(AuthServerProperties authServerProperties) {
        return new JwtTokenServerUtils(authServerProperties);
    }
}
