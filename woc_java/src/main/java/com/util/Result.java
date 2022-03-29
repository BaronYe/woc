package com.util;

import com.enums.ExceptionEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 响应操作结果
 * <pre>
 *  {
 *      code： 错误码，
 *      msg：操作消息，
 *      data：  响应数据
 *  }
 * </pre>
 */
public class Result {
    private final static String SUCCESS = "操作成功";
    private final static String ERROR = "操作失败";

    public static Object ok() {
        Map<String, Object> obj = new HashMap<>();
        obj.put("code", Code.SUCCESS);
        obj.put("msg", SUCCESS);
        return obj;
    }

    public static Object ok(Object data) {
        Map<String, Object> obj = new HashMap<>();
        obj.put("code", Code.SUCCESS);
        obj.put("msg", SUCCESS);
        obj.put("data", data);
        return obj;
    }

    public static Object ok(int code, String msg) {
        Map<String, Object> obj = new HashMap<>();
        obj.put("code", code);
        obj.put("msg", msg);
        return obj;
    }

    public static Object ok(int code, String msg, Object data) {
        Map<String, Object> obj = new HashMap<>();
        obj.put("code", code);
        obj.put("msg", msg);
        obj.put("data", data);
        return obj;
    }

    public static Object ok(Object data, int count) {
        Map<String, Object> obj = new HashMap<>();
        obj.put("code", Code.SUCCESS);
        obj.put("msg", SUCCESS);
        obj.put("data", data);
        obj.put("count", count);
        return obj;
    }

    public static Object fail(Code fail, String error) {
        Map<String, Object> obj = new HashMap<>();
        obj.put("code", Code.FAIL);
        obj.put("msg", ERROR);
        return obj;
    }

    public static Object fail(int code, String msg) {
        Map<String, Object> obj = new HashMap<>();
        obj.put("code", code);
        obj.put("msg", msg);
        return obj;
    }

    public static Object fail (ExceptionEnum v){
        Map<String, Object> obj = new HashMap<>();
        obj.put("code", v.getCode());
        obj.put("msg", v.getMsg());
        return obj;
    }

    public static Object fail() {
        Map<String, Object> obj = new HashMap<>();
        obj.put("code", Code.FAIL);
        obj.put("msg", ERROR);
        return obj;
    }

}