package com.ahsan.scrap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ahsan.scrap.util.DateUtils;

@Configuration
public class ThymeleafConfig {

    @Bean
    public DateUtils dateUtils() {
        return new DateUtils();
    }
}