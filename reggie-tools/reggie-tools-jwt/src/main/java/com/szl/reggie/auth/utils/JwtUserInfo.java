package com.szl.reggie.auth.utils;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * jwt 存储的 内容
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtUserInfo implements Serializable {
    private Long id;

    //姓名
    private String name;


    //手机号
    private String phone;


    //性别 0 女 1 男
    private String sex;


    //身份证号
    private String idNumber;


    //头像
    private String avatar;


    //状态 0:禁用，1:正常
    private Integer status;
}
