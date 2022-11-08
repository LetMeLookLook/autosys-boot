package com.autosys.common.core.exception;

import com.autosys.common.core.api.CommonResult;
import com.autosys.common.core.constants.enums.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @description 全局异常处理
 * @author jingqiu.wang
 * @date 2022年8月29日 16点34分
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 自定义业务异常捕获
     * @param exception BizException
     * @return CommonResult<String>
     */
    @ExceptionHandler(value = ApiException.class)
    public CommonResult<String> exceptionHandler(ApiException exception) {
        //打印日志
        printExceptionLog(exception);
        //返回前端
        return CommonResult.failed(exception);
    }

    /**
     * Exception拦截
     * @param exception Exception
     * @return CommonResult<String>
     */
    @ExceptionHandler(value = Exception.class)
    public CommonResult<String> exceptionHandler(Exception exception) {
        //日志打印
        printExceptionLog(exception);
        //返回前端
        return CommonResult.failed(ResultCodeEnum.SERVER_RESOURCE_ERROR, exception.getMessage());
    }
    /**
     * 校验参数Exception拦截
     * @param exception Exception
     * @return CommonResult<String>
     */
    @ExceptionHandler(BindException.class)
    public CommonResult<String> exceptionHandler(BindException exception) {
        //打印日志
        printExceptionLog(exception);
        String msg;
        FieldError error = exception.getBindingResult().getFieldError();
        if (null != error) {
            msg = error.getDefaultMessage();
        } else {
            msg = "入参异常，请检查参数";
        }
        return CommonResult.failed(ResultCodeEnum.CONSUMER_REQUEST_PARAM_ERROR,  msg);
    }
    /**
     * @param request HttpServletRequest
     * @return requestId
     */
    private String getRequestId(HttpServletRequest request) {
        return request.getHeader("requestId");
    }

    /**
     * 日志打印
     * @param exception 异常
     */
    private void printExceptionLog(Exception exception) {
        int errorCode = 500;
        if (exception instanceof ApiException) {
            errorCode = ((ApiException) exception).getResultCode();
        }
        //requestId不为空
        log.error("errorCode:{}, message:{}, errorClass:{}, errorLine:{}",
                errorCode,
                exception,
                exception.getStackTrace()[0].getClassName(),
                exception.getStackTrace()[0].getLineNumber());
        exception.printStackTrace();
    }
}
