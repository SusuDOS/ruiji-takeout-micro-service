package com.szl.reggie.auth.utils;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;

import com.szl.reggie.context.BaseContextConstants;
import com.szl.reggie.exception.BizException;
import com.szl.reggie.exception.code.ExceptionCode;
import com.szl.reggie.utils.DateUtils;
import com.szl.reggie.utils.NumberHelper;
import com.szl.reggie.utils.StrHelper;

import com.szl.reggie.context.BaseContextConstants;
import com.szl.reggie.exception.BizException;
import com.szl.reggie.exception.code.ExceptionCode;
import com.szl.reggie.utils.DateUtils;
import com.szl.reggie.utils.NumberHelper;
import com.szl.reggie.utils.StrHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class JwtHelper {
    private static final RsaKeyHelper RSA_KEY_HELPER = new RsaKeyHelper();
    /**
     * 生成用户token
     * @param jwtInfo
     * @param priKeyPath
     * @param expire
     * @return
     * @throws BizException
     */
    public static Token generateUserToken(JwtUserInfo jwtInfo, String priKeyPath, int expire) throws BizException {
        JwtBuilder jwtBuilder = Jwts.builder()
                //设置主题
                .setSubject(String.valueOf(jwtInfo.getId()))
                .claim(BaseContextConstants.JWT_KEY_NAME, jwtInfo.getName())
                .claim(BaseContextConstants.JWT_KEY_PHONE, jwtInfo.getPhone())
                .claim(BaseContextConstants.JWT_KEY_SEX, jwtInfo.getSex())
                .claim(BaseContextConstants.JWT_KEY_IDNUMBER, jwtInfo.getIdNumber())
                .claim(BaseContextConstants.JWT_KEY_AVATAR, jwtInfo.getAvatar())
                .claim(BaseContextConstants.JWT_KEY_STATUS, jwtInfo.getStatus());
        return generateToken(jwtBuilder, priKeyPath, expire);
    }


    /**
     * 生成员工token
     * @param jwtInfo
     * @param priKeyPath
     * @param expire
     * @return
     * @throws BizException
     */
    public static Token generateEmployeeToken(JwtEmployeeInfo jwtInfo, String priKeyPath, int expire) throws BizException {
        JwtBuilder jwtBuilder = Jwts.builder()
                //设置主题
                .setSubject(String.valueOf(jwtInfo.getId()))
                .claim(BaseContextConstants.JWT_KEY_USERNAME, jwtInfo.getUsername())
                .claim(BaseContextConstants.JWT_KEY_NAME, jwtInfo.getName())
                .claim(BaseContextConstants.JWT_KEY_PASSWORD, jwtInfo.getPassword())
                .claim(BaseContextConstants.JWT_KEY_PHONE, jwtInfo.getPhone())
                .claim(BaseContextConstants.JWT_KEY_SEX, jwtInfo.getSex())
                .claim(BaseContextConstants.JWT_KEY_IDNUMBER, jwtInfo.getIdNumber())
                .claim(BaseContextConstants.JWT_KEY_STATUS, jwtInfo.getStatus());
        return generateToken(jwtBuilder, priKeyPath, expire);
    }

    /**
     * 获取token中的用户信息
     * @param token      token
     * @param pubKeyPath 公钥路径
     * @return
     * @throws Exception
     */
    public static JwtUserInfo getUserJwtFromToken(String token, String pubKeyPath) throws BizException {

        Jws<Claims> claimsJws = parserToken(token, pubKeyPath);
        Claims body = claimsJws.getBody();
        String strId = body.getSubject();
        String name = StrHelper.getObjectValue(body.get(BaseContextConstants.JWT_KEY_NAME));
        String phone = StrHelper.getObjectValue(body.get(BaseContextConstants.JWT_KEY_PHONE));
        String sex = StrHelper.getObjectValue(body.get(BaseContextConstants.JWT_KEY_SEX));
        String idNumber = StrHelper.getObjectValue(body.get(BaseContextConstants.JWT_KEY_IDNUMBER));
        String avatar = StrHelper.getObjectValue(body.get(BaseContextConstants.JWT_KEY_AVATAR));
        String strStatus = StrHelper.getObjectValue(body.get(BaseContextConstants.JWT_KEY_STATUS));
        Long Id = NumberHelper.longValueOf0(strId);
        Integer status = NumberHelper.intValueOf0(strStatus);
        return new JwtUserInfo(Id,name,phone,sex,idNumber,avatar,status);
    }

    /**
     * 获取token中的用户信息
     * @param token      token
     * @param pubKeyPath 公钥路径
     * @return
     * @throws Exception
     */
    public static JwtEmployeeInfo getEmployeeJwtFromToken(String token, String pubKeyPath) throws BizException {
        Jws<Claims> claimsJws = parserToken(token, pubKeyPath);
        Claims body = claimsJws.getBody();
        String strId                  = body.getSubject();
        String username                 = StrHelper.getObjectValue(body.get(BaseContextConstants.JWT_KEY_USERNAME));
        String name                = StrHelper.getObjectValue(body.get(BaseContextConstants.JWT_KEY_NAME));
        String password               = StrHelper.getObjectValue(body.get(BaseContextConstants.JWT_KEY_PASSWORD));
        String phone      = StrHelper.getObjectValue(body.get(BaseContextConstants.JWT_KEY_PHONE));
        String sex                   = StrHelper.getObjectValue(body.get(BaseContextConstants.JWT_KEY_SEX));
        String idNumber                    = StrHelper.getObjectValue(body.get(BaseContextConstants.JWT_KEY_IDNUMBER));
        String strStatus            = StrHelper.getObjectValue(body.get(BaseContextConstants.JWT_KEY_STATUS));
        Long id = NumberHelper.longValueOf0(strId);
        Integer status = NumberHelper.intValueOf0(strStatus);
        return new JwtEmployeeInfo(id, username, name, password, phone,sex,idNumber,status);
    }
    /**
     * 生成token
     * @param builder
     * @param priKeyPath
     * @param expire
     * @return
     * @throws BizException
     */
    protected static Token generateToken(JwtBuilder builder, String priKeyPath, int expire) throws BizException {
        try {
            //返回的字符串便是我们的jwt串了
            String compactJws = builder.setExpiration(DateUtils.localDateTime2Date(LocalDateTime.now().plusSeconds(expire)))
                    //设置算法（必须）
                    .signWith(SignatureAlgorithm.RS256, RSA_KEY_HELPER.getPrivateKey(priKeyPath))
                    //这个是全部设置完成后拼成jwt串的方法
                    .compact();
            return new Token(compactJws, expire);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("errcode:{}, message:{}", ExceptionCode.JWT_GEN_TOKEN_FAIL.getCode(), e.getMessage());
            throw new BizException(ExceptionCode.JWT_GEN_TOKEN_FAIL.getCode(), ExceptionCode.JWT_GEN_TOKEN_FAIL.getMsg());
        }
    }

    /**
     * 公钥解析token
     * @param token
     * @param pubKeyPath 公钥路径
     * @return
     * @throws Exception
     */
    private static Jws<Claims> parserToken(String token, String pubKeyPath) throws BizException {
        try {
            return Jwts.parser().setSigningKey(RSA_KEY_HELPER.getPublicKey(pubKeyPath)).parseClaimsJws(token);
        } catch (ExpiredJwtException ex) {
            //过期
            throw new BizException(ExceptionCode.JWT_TOKEN_EXPIRED.getCode(), ExceptionCode.JWT_TOKEN_EXPIRED.getMsg());
        } catch (SignatureException ex) {
            //签名错误
            throw new BizException(ExceptionCode.JWT_SIGNATURE.getCode(), ExceptionCode.JWT_SIGNATURE.getMsg());
        } catch (IllegalArgumentException ex) {
            //token 为空
            throw new BizException(ExceptionCode.JWT_ILLEGAL_ARGUMENT.getCode(), ExceptionCode.JWT_ILLEGAL_ARGUMENT.getMsg());
        } catch (Exception e) {
            log.error("errcode:{}, message:{}", ExceptionCode.JWT_PARSER_TOKEN_FAIL.getCode(), e.getMessage());
            throw new BizException(ExceptionCode.JWT_PARSER_TOKEN_FAIL.getCode(), ExceptionCode.JWT_PARSER_TOKEN_FAIL.getMsg());
        }
    }
}
