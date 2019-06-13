package com.github.nicolasperuch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket swaggerRulingApi(){
        return new Docket(SWAGGER_2)
                    .select()
                    .apis(basePackage("com.github.nicolasperuch.api"))
                    .paths(regex(".*"))
                    .build();
    }
}
