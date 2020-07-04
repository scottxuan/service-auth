package com.service.auth.swagger;

import com.scottxuan.web.swagger.AbstractSwaggerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author : zhaoxuan
 * @date : 2020/6/23
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends AbstractSwaggerConfig {

    private static final String BASEPACKAGE = "com.service.auth.api";
    private static final String TITLE = "SERVICE-AUTH Restful API Docs";
    private static final String DESCRIPTION = "SERVICE-AUTH Restful API Docs";
    private static final String VERSION = "1.0";

    @Bean
    @Override
    public Docket createRestApi() {
        return super.createRestApi();
    }

    @Override
    protected String getBasePackage() {
        return BASEPACKAGE;
    }

    @Override
    protected String getTitle() {
        return TITLE;
    }

    @Override
    protected String getDescription() {
        return DESCRIPTION;
    }

    @Override
    protected String getVersion() {
        return VERSION;
    }
}
