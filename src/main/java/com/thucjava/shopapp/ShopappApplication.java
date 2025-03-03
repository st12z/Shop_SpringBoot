package com.thucjava.shopapp;

import com.thucjava.shopapp.config.VNPayConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@ConfigurationPropertiesScan

public class ShopappApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopappApplication.class, args);
	}

}
