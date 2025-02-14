package com.thucjava.shopapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModalMapperConfig {
    @Bean
    public ModalMapperConfig modelMapper() {
        return new ModalMapperConfig();
    }
}

