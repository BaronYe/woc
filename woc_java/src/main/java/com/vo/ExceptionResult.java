package com.vo;
import com.enums.ExceptionEnum;
import lombok.Getter;

@Getter
public class ExceptionResult {
    public Integer code;
    public String errors;
    public  ExceptionResult(ExceptionEnum v){
        this.code = v.getCode();
        this.errors = v.getMsg();
    }
}
