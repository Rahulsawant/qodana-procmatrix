package com.qodana.matrixrotation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.qodana.matrixrotation.config")
public class MatrixRotationApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatrixRotationApplication.class, args);
	}

}
