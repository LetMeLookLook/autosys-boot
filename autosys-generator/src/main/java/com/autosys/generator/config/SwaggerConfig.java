package com.autosys.generator.config;

import com.autosys.common.core.config.BaseSwaggerConfig;
import com.autosys.common.core.domain.SwaggerProperties;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @description Swagger API文档相关配置
 * @author jingqiu.wang
 * @date 2022年8月29日 16点34分
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.autosys.system")
                .title("Autosys-Boot 系统管理服务")
                .description("")
                .contactName("Autosys")
                .version("1.0")
                .enableSecurity(false)
                .build();
    }

    @Bean
    public BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
        return generateBeanPostProcessor();
    }

}
