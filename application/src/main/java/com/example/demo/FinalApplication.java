package com.example.demo;

import com.geekgame.demo.util.ApplicationContextProvider;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = {"com.example.demo","com.geekgame.demo"},exclude = {DataSourceAutoConfiguration.class})
@MapperScan(basePackages = {"com.geekgame.demo.dao"})
@EnableDiscoveryClient
public class FinalApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(FinalApplication.class, args);
		ApplicationContextProvider.set(context);
	}

}
