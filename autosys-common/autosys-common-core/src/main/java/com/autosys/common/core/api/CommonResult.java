package com.autosys.common.core.api;

import com.autosys.common.core.constants.enums.ResultCodeEnum;
import com.autosys.common.core.exception.ApiException;
import io.swagger.annotations.ApiModelProperty;
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
    private Integer code;
    /**
     * 提示信息
     */
    private String message;

    /**
     * 结果码 四位数字
     */
    @ApiModelProperty(value = "结果码", position = 1, example = "1001")
    private Integer resultCode;

    /**
     * code 成功
     */
    private static final int SUCCESS = 0;

    /**
     * code 失败
     */
    private static final int ERROR = 1;

    /**
     * 数据封装
     */
    private T data;

    protected CommonResult() {
        this.code = SUCCESS;
        this.resultCode = ResultCodeEnum.SUCCESS.getResultCode();
    }

    protected CommonResult(T data){
        this();
        this.data = data;
    }

    protected CommonResult(Integer code, String message, T data) {
        this(code,message);
        this.data = data;
    }

    private CommonResult(Integer resultCode, String message) {
        this.message = message;
        this.code = ERROR;
        this.resultCode = resultCode;
    }

    /**
     * 成功返回结果（无参）
     */
    public static <T> CommonResult<T> success() {
        return new CommonResult<T>();
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>(data);
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
     * 返回失败，根据ApiException异常
     * @param e ApiException异常
     * @param <T> 泛型
     * @return CommonResult 统一返回结果
     */
    public static <T> CommonResult<T> failed(ApiException e) {
        return new CommonResult<>(e.getResultCode(), e.getMessage());
    }

}
