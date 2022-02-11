package com.example.miaosha1.vo;


import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public class LoginVo {
    @NotNull
    private long mobile;

    @NotNull
    @Length(min = 32)
    private String password;


    public long getMobile() {
        return mobile;
    }

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginVo{" +
                "mobile=" + mobile +
                ", password='" + password + '\'' +
                '}';
    }
}
