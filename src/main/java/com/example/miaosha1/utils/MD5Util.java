package com.example.miaosha1.utils;


import org.springframework.util.DigestUtils;


public class MD5Util {
    public static String md5(String pass){
        byte[] passBytes=pass.getBytes();
        return DigestUtils.md5DigestAsHex(passBytes);
    }

    public static final String salt="1a2b3c4d";

    /*inputPass:输入密码 FormPass:表单密码  DbPass：数据库密码*/

    public static String inputPassToFormPass(String inputPass){
        String pass=""+salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(5) + salt.charAt(4);
        return md5(pass);
    }
    public static String formPassToDbPass(String formPass,String salt){
        String dbPass=salt.charAt(1)+formPass+salt.charAt(2)+salt.charAt(3);
        return  md5(dbPass);
    }
    public static String inputPassToDbPass(String inputPass,String salt){
        return formPassToDbPass(inputPassToFormPass(inputPass),salt);
    }
    public static void main(String[] args) {
        System.out.println(inputPassToFormPass("asdfgh"));
        System.out.println(formPassToDbPass(inputPassToFormPass("asdfgh"), "abcd"));
        System.out.println(inputPassToDbPass("asdfgh", "abcd"));
    }


}
