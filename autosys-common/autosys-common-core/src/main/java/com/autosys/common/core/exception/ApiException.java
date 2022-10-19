package com.autosys.common.core.exception;

import com.autosys.common.core.api.IErrorCode;
import com.autosys.common.core.constants.enums.ResultCodeEnum;
import lombok.Data;

/**
 * @description 自定义API异常
 * @author jingqiu.wang
 * @date 2022年8月29日 16点34分
 */
@Data
public class ApiException extends RuntimeException {
    /**
     * 错误码
     */
    private int resultCode;

    private ApiException() {
        super();
    }

    /**
     * 使用自定义状态码描述
     * @param resultCode 结果码
     * @param message 状态码描述
     */
    public ApiException(int resultCode, String message) {
        super(message);
        this.resultCode = resultCode;
    }

    /**
     * 使用自定义状态码描述 + Throwable
     * @param resultCode 结果码
     * @param message 状态码描述
     * @param cause Throwable
     */
    public ApiException(int resultCode, String message, Throwable cause) {
        super(message, cause);
        this.resultCode = resultCode;
    }

    /**
     * 直接使用结果码枚举类
     * @param resultCodeEnum 结果码枚举类
     */
    public ApiException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.resultCode = resultCodeEnum.getResultCode();
    }

    /**
     * 直接使用结果码枚举类 + Throwable
     * @param resultCodeEnum 结果码枚举类
     * @param cause Throwable
     */
    public ApiException(ResultCodeEnum resultCodeEnum, Throwable cause) {
        super(resultCodeEnum.getMessage(), cause);
        this.resultCode = resultCodeEnum.getResultCode();
    }

    /**
     * 重新异常栈
     * @return Throwable
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[6];
        StackTraceElement[] stackTraceElements = {new StackTraceElement(stackTraceElement.getClassName(), stackTraceElement.getMethodName()
                , stackTraceElement.getFileName(), stackTraceElement.getLineNumber())};
        this.setStackTrace(stackTraceElements);
        return this;
    }
}
