package com.stylefeng.guns.rest.config.common;

import com.stylefeng.guns.rest.config.properties.RestProperties;
import com.stylefeng.guns.rest.core.filter.AuthFilter;
import com.stylefeng.guns.rest.core.filter.CrossFilter;
import com.stylefeng.guns.rest.modular.auth.security.DataSecurityAction;
import com.stylefeng.guns.rest.modular.auth.security.impl.Base64SecurityAction;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * web配置
 *
 * @author fengshuonan
 * @date 2017-08-23 15:48
 */
@Configuration
public class WebConfig {

    @Bean
    @ConditionalOnProperty(prefix = RestProperties.REST_PREFIX, name = "auth-open", havingValue = "true", matchIfMissing = true)
    public AuthFilter jwtAuthenticationTokenFilter() {
        return new AuthFilter("/api/**");
    }

    @Bean
    public DataSecurityAction dataSecurityAction() {
        return new Base64SecurityAction();
    }

    @Bean
    public CrossFilter crossFilter() {
        return new CrossFilter();
    }

    /**
     * Bean转换工具类
     *
     * @author: lx
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
