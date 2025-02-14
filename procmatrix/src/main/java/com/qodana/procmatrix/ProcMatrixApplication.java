package com.qodana.procmatrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(basePackages = {"com.qodana.procmatrix", "com.procmatrix"})
@PropertySource({"classpath:application-core.properties", "classpath:application.properties"})
public class ProcMatrixApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProcMatrixApplication.class, args);
    }

}
