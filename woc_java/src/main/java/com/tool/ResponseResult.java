package com.tool;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/*
 * 返回数据封装
 * 第一步注解
 * */
@Retention(RUNTIME)
@Target({TYPE,METHOD})
@Documented
public @interface ResponseResult {
}
