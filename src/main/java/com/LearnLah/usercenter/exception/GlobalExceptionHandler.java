package com.LearnLah.usercenter.exception;

import com.LearnLah.usercenter.common.BaseResponse;
import com.LearnLah.usercenter.common.ErrorCode;
import com.LearnLah.usercenter.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * global exception handler
 *
 * @author YanChenyi
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Catch exceptions thrown by BusinessException
     * @param e exceptions thrown by BusinessException
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e) {
        log.error("BusinessException" + e.getMessage(), e);
        return ResultUtil.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return ResultUtil.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }

}
