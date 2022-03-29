package com.config;


import com.enums.ExceptionEnum;
import com.util.Code;
import com.util.Result;
import com.vo.ExceptionResult;
import com.vo.MyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.net.BindException;
import java.util.Iterator;
import java.util.Set;

/***
 * @Description: //TODO 异常捕获类
 * @author: songxinyong
 * @date: 2020/11/10 21:44
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 自定义异常, 查询结果为空, 或者写入数据失败时调用
     * @param e {@link MyException}
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler(value = MyException.class)
    public Object myExceptionHandler(MyException e) {
        ExceptionEnum exceptionEnum = e.getExceptionEnum();
        int code = exceptionEnum.getCode();
        ExceptionResult exceptionResult = new ExceptionResult(exceptionEnum);
        return Result.fail(code,exceptionResult.getErrors());
    }

    /**
     * throw new Exception(e.getMessage()); 捕获异常处理
     */
//    @ExceptionHandler(Exception.class)
//    public Object exceptionHandler(Exception  ex) {
//        String error;
//        if (ex.getMessage() == null){
//            error = "空指针异常";
//        }else{
//            error = ex.getMessage();
//        }
//        return Result.fail(Code.FAIL,error);
//    }


    /**
     * 单个参数验证适用于Controller的验证
     *
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Object ConstraintViolationExceptionHandler(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        Iterator<ConstraintViolation<?>> iterator = constraintViolations.iterator();
        String error = "";
        while (iterator.hasNext()) {
            ConstraintViolation<?> cvl = iterator.next();
            error = cvl.getMessageTemplate();
        }
        return Result.fail(Code.FAIL,error);
    }
}
