package com.yakiyo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "약이요 API",
        version = "1.0",
        description = "약 복용 스케줄링 시스템 API 문서"
))
public class YakiyoApplication {

    public static void main(String[] args) {
        SpringApplication.run(YakiyoApplication.class, args);
    }

}
