package com.szl.reggie.auth.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * jwt 存储的 内容
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtEmployeeInfo implements Serializable {
    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;

    private Integer status;
}
