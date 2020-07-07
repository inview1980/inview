package com.example.mymanageclient.tool;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.AES256TextEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;

import java.security.MessageDigest;

/**
 * 加密算法util
 * Created on 2020/3/23.
 * StrongTextEncryptor 64位,BasicTextEncryptor 24位
 */
public final class SecretUtil {
    public static String UserName;
    public static String Password;
    public static String SessionId;


    public static final String getEncryptString(String pwd, String word) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        //设置加密密钥
        textEncryptor.setPassword(pwd);

        //加密信息
        return textEncryptor.encrypt(word);
    }

    public static final String getDecryptSting(String pwd, String word) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
            //设置解密密钥
            textEncryptor.setPassword(pwd);
            //解密
        return textEncryptor.decrypt(word);
    }


}
