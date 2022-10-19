package com.autosys.common.core.constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description 结果码枚举类，1xxx表示用户端异常，2xxx表示服务端异常，3xxx表示中间件或三方服务异常，9999代表正常
 * @author jingqiu.wang
 * @date 2022年8月29日 16点34分
 */
@AllArgsConstructor
@Getter
public enum ResultCodeEnum {

    /**
     * 用户注册异常，如：用户名已存在，用户名包含敏感词等
     */
    CONSUMER_REGISTER_ERROR(1100, "用户注册异常"),

    /**
     * 用户登录异常，如：用户账户不存在，用户密码错误等
     */
    CONSUMER_LOGIN_ERROR(1200, "用户登录异常"),

    /**
     * 用户登录异常,用户不存在
     */
    CONSUMER_LOGIN_ERROR_NOTEXITS(1210, "用户登录异常,用户不存在"),

    /**
     * 用户登录异常,用户不存在角色信息
     */
    CONSUMER_LOGIN_ERROR_NOROLES(1220, "用户登录异常,用户不存在角色信息"),

    /**
     * 用户登录异常,请求未携带Token
     */
    CONSUMER_LOGIN_ERROR_NOTOKEN(1230, "用户登录异常,请求未携带Token"),

    /**
     * 用户登录异常,无效Token
     */
    CONSUMER_LOGIN_ERROR_FAILTOKEN(1240, "用户登录异常,无效Token"),

    /**
     * 用户登录异常,密码错误
     */
    CONSUMER_LOGIN_ERROR_PASSWORD(1250, "用户登录异常,用户名或密码错误"),

    /**
     * 用户登录异常,用户被锁定
     */
    CONSUMER_LOGIN_ERROR_LOCKED(1260, "用户登录异常,用户被锁定"),

    /**
     * 用户登录异常,用户被禁用
     */
    CONSUMER_LOGIN_ERROR_DISABLED(1270, "用户登录异常,用户被禁用"),

    /**
     * 用户访问权限异常，如：访问未授权，网关访问受限，用户访问被拦截等
     */
    CONSUMER_AUTHORITY_ERROR(1300,"用户访问权限异常"),

    /**
     * 用户请求参数异常，如：无效的用户输入，请求必填参数为空等
     */
    CONSUMER_REQUEST_PARAM_ERROR(1400,"用户请求参数异常"),

    /**
     * 用户请求服务异常，如：请求次数超出限制，用户重复请求等
     */
    CONSUMER_REQUEST_SERVICE_ERROR(1500,"用户请求服务异常"),

    /**
     * 用户资源异常，如：账户余额不足有，用户磁盘空间不足等
     */
    CONSUMER_RESOURCE_ERROR(1600,"用户资源异常"),

    /**
     * 用户上传文件异常，如：用户上传文件类型不匹配，用户上传文件太大等
     */
    CONSUMER_UPLOAD_ERROR(1700,"用户上传文件异常"),

    /**
     * 系统执行超时，如：处理xxx业务超时
     */
    SERVER_TIMEOUT_ERROR(2100,"系统执行超时"),

    /**
     * 系统资源异常，如：系统连接池耗尽，系统内存耗尽等
     */
    SERVER_RESOURCE_ERROR(2300, "系统资源异常"),

    /**
     * 中间件服务出错，如：RPC服务未找到，服务未注册，接口不存在等
     */
    THIRD_PART_SERVER_ERROR(3100, "中间件服务出错"),

    /**
     * 第三方系统执行超时，如：RPC/HTTP执行超时，配置服务超时，数据库服务超时等
     */
    THIRD_PART_TIMEOUT_ERROR(3200, "第三方系统执行超时"),

    /**
     * 数据库服务出错，如：表不存在，列不存在，死锁，主键冲突等
     */
    THIRD_PART_DB_ERROR(3300, "数据库服务出错"),

    /**
     * 通知服务出错，如：短信提醒失败，邮件提醒失败等
     */
    THIRD_PART_NOTIFY_ERROR(3500, "通知服务出错"),

    /**
     * 成功返回
     */
    SUCCESS(9999, "success"),

    /**
     * 失败返回
     */
    FAILED(500,"failed");

    /**
     * 结果码
     */
    private final Integer resultCode;

    /**
     * 描述
     */
    private final String message;

}
