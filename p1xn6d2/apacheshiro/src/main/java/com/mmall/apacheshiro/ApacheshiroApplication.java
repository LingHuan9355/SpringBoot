package com.mmall.apacheshiro;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
@MapperScan(basePackages = {"com.mmall.apacheshiro.mapper"})
public class ApacheshiroApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApacheshiroApplication.class, args);
	}

}
