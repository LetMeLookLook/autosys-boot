package com.autosys.common.core.constants;

/**
 * @description 公共静态变量
 * @author jingqiu.wang
 * @date 2022年9月15日 15点58分
 */
public interface CommonConstant {

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * Http Header token
     */
    String X_ACCESS_TOKEN = "X-Access-Token";

    /**
     * 微服务读取配置文件属性 服务地址
     */
    String CLOUD_SERVER_KEY = "spring.cloud.nacos.discovery.server-addr";

    /**
     * 系统用户状态-启用
     */
    String SYS_USER_STATUS_ENABLE = "ENABLE";
    /**
     * 系统用户状态-停用
     */
    String SYS_USER_STATUS_DISABLE = "DISABLE";
    /**
     * 系统用户状态-锁定
     */
    String SYS_USER_STATUS_LOCKED = "LOCKED";

    String MSG_TOKEN_IS_INVALID = "Token失效，请重新登录!";

    String PREFIX_USER_TOKEN  = "PREFIX_USER_TOKEN_";

    /**
     * 用户缓存信息
     */
    String CACHE_SYS_USER  = "CACHE_SYS_USER";

    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";
}
