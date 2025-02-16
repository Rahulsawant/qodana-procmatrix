package com.procmatrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(basePackages = {"com.procmatrix.core", "com.procmatrix"})
@PropertySource({"classpath:application-core.properties", "classpath:application.properties"})
public class ProcMatrixApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProcMatrixApplication.class, args);
    }

}
