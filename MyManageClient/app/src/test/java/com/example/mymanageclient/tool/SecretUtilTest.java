package com.example.mymanageclient.tool;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.AES256TextEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;
import org.junit.Test;

import static org.junit.Assert.*;

import org.jasypt.util.text.BasicTextEncryptor;

/**
 * @author inview
 * @Date 2020/6/30 10:12
 * @Description :
 */
public class SecretUtilTest {
    private String pwd  = "123456";
    private String word = "4321";

    @Test
    public void encry() {
        BasicTextEncryptor  textEncryptor = new BasicTextEncryptor();
        AES256TextEncryptor e2            = new AES256TextEncryptor();
        StrongTextEncryptor e3            = new StrongTextEncryptor();
        //设置加密密钥
        textEncryptor.setPassword(pwd);
        e2.setPassword(pwd);
        e3.setPassword(pwd);

        //加密信息
        String s1 = textEncryptor.encrypt(word);
        String s2 = e2.encrypt(word);
        String s3 = e3.encrypt(word);
        System.out.println(s1 + ":" + s1.length());
        System.out.println(s2 + ":" + s2.length());
        System.out.println(s3 + ":" + s3.length());

    }

    @Test
    public void t2() {
        BasicPasswordEncryptor  e1 = new BasicPasswordEncryptor();
        StrongPasswordEncryptor e2 = new StrongPasswordEncryptor();
        String                  s1 = e1.encryptPassword(pwd);
        String                  s2 = e2.encryptPassword(pwd);
        System.out.println(s1 + ":" + s1.length());
        System.out.println(s2 + ":" + s2.length());
        String s3 = e2.encryptPassword(s1);
        System.out.println(s3 + ":" + s3.length());
    }

    @Test
    public  void t3(){

        BasicPasswordEncryptor basicPasswordEncryptor = new BasicPasswordEncryptor();
        StrongPasswordEncryptor strongPasswordEncryptor = new StrongPasswordEncryptor();
        int i1=2;
            String p1 = basicPasswordEncryptor.encryptPassword(String.valueOf(i1));
            String p2 = strongPasswordEncryptor.encryptPassword(p1);
        System.out.println(strongPasswordEncryptor.checkPassword("VvxBelQVJoYpQC8R6KhMr/DLWbZ5CQfh",
                "O6SmItZ0u8WI7R3IcaExxERG57KuRBagR0IH4sHpqLLSnZWj86kBlIrbNr7sAF7F"));
    }
}