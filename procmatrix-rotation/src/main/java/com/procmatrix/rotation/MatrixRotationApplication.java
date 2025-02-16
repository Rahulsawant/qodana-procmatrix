package com.procmatrix.rotation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(basePackages = {"com.procmatrix.core", "com.procmatrix.rotation"})
@PropertySource({"classpath:application-core.properties", "classpath:application.properties"})

public class MatrixRotationApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatrixRotationApplication.class, args);
	}

}
