package com.autosys.common.core.config;

import com.autosys.common.core.domain.Shiro;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * @description 通用基础配置
 * @author jingqiu.wang
 * @date 2022年8月29日 16点34分
 */
@Data
@Component("autosysBaseConfig")
@ConfigurationProperties(prefix = "autosys")
public class AutosysBaseConfig {
    /**
     * shiro配置信息
     */
    private Shiro shiro;
}
