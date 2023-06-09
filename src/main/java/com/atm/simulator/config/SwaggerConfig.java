package com.atm.simulator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket productApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.atm.simulator"))
                .paths(regex("/atm.*"))
                .build()
                .apiInfo(metaInfo());
    }


    private ApiInfo metaInfo(){
        ApiInfo apiInfo =  new ApiInfo(
                "AtmData",
                "AtmData Simulator Spring boot project",
                "1.0",
                ".",
                new Contact("Lena Sargsyan",".",
                        "."),
                ".",
                "."
        );
        return apiInfo;
    }
}
