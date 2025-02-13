package com.qodana.matrixrotation.config;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.hateoas.EntityModel;

//d@Configuration
public class JacksonConfig {
/*
    @Bean
    public SimpleModule entityModelModule() {
        SimpleModule module = new SimpleModule();
        // Add a specific serializer for EntityModel<int[][]>
        module.addSerializer(EntityModel.class, new EntityModelMatrixSerializer());
        return module;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(entityModelModule());  // Register the custom serializer module
        return objectMapper;
    }*/
}
