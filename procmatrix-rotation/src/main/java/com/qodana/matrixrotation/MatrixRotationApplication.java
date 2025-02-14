package com.qodana.matrixrotation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(basePackages = {"com.qodana.matrixrotation", "com.procmatrix"})
@PropertySource({"classpath:application.properties", "classpath:procmatrix-core/application.properties"})

public class MatrixRotationApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatrixRotationApplication.class, args);
	}

}
