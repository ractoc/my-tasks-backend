package com.ractoc.mytasksbackend.tasks;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.function.Predicate;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket produceApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ractoc.mytasksbackend"))
                .paths(testPaths()::test)
                .build();
    }

    // Describe your apis
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("My-Tasks Tasks Rest APIs")
                .description("This page lists all the rest apis for My-Tasks Tasks endpoint.")
                .version("1.0.0")
                .build();
    }

    // Only select apis that matches the given Predicates.
    private Predicate<String> testPaths() {
// Match all testPaths except /error
        return testPath("/task.*").and((testPath("/error.*")).negate());
    }

    private Predicate<String> testPath(String s) {
        return PathSelectors.regex(s)::apply;
    }
}
