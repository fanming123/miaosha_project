package com.example.miaosha1.exception;


import com.example.miaosha1.result.CodeMsg;
import com.example.miaosha1.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public Result<Boolean> exceptionHandler(HttpServletRequest request, Exception e){
        if (e instanceof BindException){
            BindException exception=(BindException)e;
            List<ObjectError> errors=exception.getAllErrors();
            ObjectError error=errors.get(0);
            String msg=error.getDefaultMessage();
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
        }
        else {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
