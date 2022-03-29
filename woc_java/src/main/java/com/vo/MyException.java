package com.vo;

import com.enums.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 自定义异常类
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyException extends RuntimeException {
    private ExceptionEnum exceptionEnum;
}
