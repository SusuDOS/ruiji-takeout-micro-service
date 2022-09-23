package com.szl.reggie.auth.client.utils;

import com.szl.reggie.auth.client.properties.AuthClientProperties;
import com.szl.reggie.auth.utils.*;
import com.szl.reggie.exception.BizException;

import com.szl.reggie.auth.utils.JwtHelper;
import com.szl.reggie.auth.utils.JwtUserInfo;
import com.szl.reggie.exception.BizException;
import lombok.AllArgsConstructor;

/**
 * JwtToken 客户端工具
 *
 */
@AllArgsConstructor
public class JwtTokenClientUtils {
    /**
     * 用于 认证服务的 客户端使用（如 网关） ， 在网关获取到token后，
     * 调用此工具类进行token 解析。
     * 客户端一般只需要解析token 即可
     */
    private AuthClientProperties authClientProperties;

    /**
     * 解析token
     *
     * @param token
     * @return
     * @throws BizException
     */
    public JwtUserInfo getUserInfo(String token) throws BizException {
        AuthClientProperties.TokenInfo userTokenInfo = authClientProperties.getUser();
        return JwtHelper.getUserJwtFromToken(token, userTokenInfo.getPubKey());
    }

    public JwtEmployeeInfo getEmployeeInfo(String token) throws BizException {
        AuthClientProperties.TokenInfo employeeTokenInfo = authClientProperties.getUser();
        return JwtHelper.getEmployeeJwtFromToken(token, employeeTokenInfo.getPubKey());
    }
}
