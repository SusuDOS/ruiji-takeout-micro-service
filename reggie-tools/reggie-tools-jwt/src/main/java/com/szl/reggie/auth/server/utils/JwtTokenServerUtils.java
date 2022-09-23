package com.szl.reggie.auth.server.utils;
import com.szl.reggie.auth.server.properties.AuthServerProperties;
import com.szl.reggie.auth.utils.*;
import com.szl.reggie.exception.BizException;

import com.szl.reggie.auth.server.properties.AuthServerProperties;
import com.szl.reggie.auth.utils.JwtHelper;
import com.szl.reggie.auth.utils.JwtUserInfo;
import com.szl.reggie.auth.utils.Token;
import com.szl.reggie.exception.BizException;
import lombok.AllArgsConstructor;
/**
 * jwt token 工具
 *
 */
@AllArgsConstructor
public class JwtTokenServerUtils {
    /**
     * 认证服务端使用，如 authority-server
     * 生成和 解析token
     */
    private AuthServerProperties authServerProperties;

    /**
     * 生成token
     * @param jwtInfo
     * @param expire
     * @return
     * @throws BizException
     */
    public Token generateUserToken(JwtUserInfo jwtInfo, Integer expire) throws BizException {
        AuthServerProperties.TokenInfo userTokenInfo = authServerProperties.getUser();
        if (expire == null || expire <= 0) {
            expire = userTokenInfo.getExpire();
        }
        return JwtHelper.generateUserToken(jwtInfo, userTokenInfo.getPriKey(), expire);
    }


    /**
     * 生成token
     * @param jwtInfo
     * @param expire
     * @return
     * @throws BizException
     */
    public Token generateEmployeeToken(JwtEmployeeInfo jwtInfo, Integer expire) throws BizException {
        AuthServerProperties.TokenInfo employeeTokenInfo = authServerProperties.getUser();
        if (expire == null || expire <= 0) {
            expire = employeeTokenInfo.getExpire();
        }
        return JwtHelper.generateEmployeeToken(jwtInfo, employeeTokenInfo.getPriKey(), expire);
    }


    /**
     * 解析token
     * @param token
     * @return
     * @throws BizException
     */
    public JwtUserInfo getUserInfo(String token) throws BizException {
        AuthServerProperties.TokenInfo userTokenInfo = authServerProperties.getUser();
        return JwtHelper.getUserJwtFromToken(token, userTokenInfo.getPubKey());
    }

    /**
     * 解析token
     * @param token
     * @return
     * @throws BizException
     */
    public JwtEmployeeInfo getEmployeeInfo(String token) throws BizException {
        AuthServerProperties.TokenInfo employeeTokenInfo = authServerProperties.getUser();
        return JwtHelper.getEmployeeJwtFromToken(token, employeeTokenInfo.getPubKey());
    }
}
