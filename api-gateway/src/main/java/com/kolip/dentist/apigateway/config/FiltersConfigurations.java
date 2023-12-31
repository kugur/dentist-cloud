package com.kolip.dentist.apigateway.config;

import com.kolip.dentist.apigateway.config.filters.UserInfoGlobalFilter;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration file to configure and added filters.
 */
@Configuration
public class FiltersConfigurations {

    @Bean
    public GlobalFilter customFilter() {
        return new UserInfoGlobalFilter();
    }
}
