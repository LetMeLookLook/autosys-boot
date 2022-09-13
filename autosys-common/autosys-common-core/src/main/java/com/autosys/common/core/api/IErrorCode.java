package com.autosys.common.core.api;

/**
 * @description 常用API返回对象接口
 * @author jingqiu.wang
 * @date 2022年8月29日 16点34分
 */
public interface IErrorCode {
    /**
     * 返回码
     */
    long getCode();

    /**
     * 返回信息
     */
    String getMessage();
}
