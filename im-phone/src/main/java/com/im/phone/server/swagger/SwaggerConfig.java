package com.im.phone.server.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * SwaggerConfig
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket demoApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .genericModelSubstitutes(DeferredResult.class)
//              .genericModelSubstitutes(ResponseEntity.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.im.phone.server.controller.apply"))
                .paths(PathSelectors.any())//过滤的接口
                .build()
                .apiInfo(demoApiInfo());
    }


    private ApiInfo demoApiInfo() {
        return new ApiInfoBuilder()
            .title("IM系统接口API")//大标题
            .version("1.0")//版本
            .contact(new Contact("师奇隆", "jack1053996819@163.com", null))//作者
            .build();
    }
}
