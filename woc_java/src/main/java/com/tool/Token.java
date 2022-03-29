package com.tool;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.entity.Admin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Date;

/***
 * @Description: //TODO 生成token
 * @param:
 * @author: songxinyong
 * @date: 2020/11/4 23:00
 * @return:
 */
public class Token {
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PassToken {
        boolean required() default true;
    }
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface LoginToken {
        boolean required() default true;
    }
    public static String getToken(Admin admin) {
        String token="";
        token= JWT.create().withAudience(String.valueOf(admin.getAccount()))
                .withExpiresAt(new Date(System.currentTimeMillis()+720000000))
                .sign(Algorithm.HMAC256(String.valueOf(admin.getAccount())));
        return token;
    }
}
