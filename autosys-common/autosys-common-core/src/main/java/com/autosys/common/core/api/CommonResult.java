package com.autosys.common.core.api;

import com.autosys.common.core.constants.enums.ResultCodeEnum;
import com.autosys.common.core.exception.ApiException;
import lombok.Data;

/**
 * @description 通用返回对象
 * @author jingqiu.wang
 * @date 2022年8月29日 16点34分
 */
@Data
public class CommonResult<T> {
    /**
     * 状态码
     */
    private long code;
    /**
     * 提示信息
     */
    private String message;
    /**
     * 数据封装
     */
    private T data;

    protected CommonResult() {
    }

    protected CommonResult(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private CommonResult(int resultCode, String message) {
        this.message = message;
        this.code = resultCode;
    }

    /**
     * 成功返回结果（无参）
     */
    public static <T> CommonResult<T> success() {
        return new CommonResult<T>(ResultCodeEnum.SUCCESS.getResultCode(), ResultCodeEnum.SUCCESS.getMessage(),null);
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>(ResultCodeEnum.SUCCESS.getResultCode(), ResultCodeEnum.SUCCESS.getMessage(), data);
    }


    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     * @param  message 提示信息
     */
    public static <T> CommonResult<T> success(T data, String message) {
        return new CommonResult<T>(ResultCodeEnum.SUCCESS.getResultCode(), message, data);
    }

    /**
     * 失败返回结果
     * @param resultCodeEnum 错误码
     */
    public static <T> CommonResult<T> failed(ResultCodeEnum resultCodeEnum) {
        return new CommonResult<T>(resultCodeEnum.getResultCode(), resultCodeEnum.getMessage(), null);
    }

    /**
     * 失败返回结果
     * @param resultCodeEnum 错误码
     * @param message 错误信息
     */
    public static <T> CommonResult<T> failed(ResultCodeEnum resultCodeEnum, String message) {
        return new CommonResult<T>(resultCodeEnum.getResultCode(), message, null);
    }

    /**
     * 失败返回结果
     * @param message 提示信息
     */
    public static <T> CommonResult<T> failed(String message) {
        return new CommonResult<T>(ResultCodeEnum.FAILED.getResultCode(), message, null);
    }

    /**
     * 失败返回结果
     */
    public static <T> CommonResult<T> failed() {
        return failed(ResultCodeEnum.FAILED);
    }

    /**
     * 返回失败，根据ApiException异常
     * @param e ApiException异常
     * @param <T> 泛型
     * @return RestApiRes 统一返回结果
     */
    public static <T> CommonResult<T> failed(ApiException e) {
        return new CommonResult<>(e.getResultCode(), e.getMessage());
    }

}
