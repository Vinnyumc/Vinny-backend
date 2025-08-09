package com.vinny.backend.config;

import com.vinny.backend.auth.annotation.CurrentUserArgumentResolver;
import com.vinny.backend.common.validator.ValidPageParamResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final CurrentUserArgumentResolver currentUserArgumentResolver;
    private final ValidPageParamResolver validPageParamResolver;

    public WebConfig(CurrentUserArgumentResolver resolver, ValidPageParamResolver validPageParamResolver) {
        this.currentUserArgumentResolver = resolver;
        this.validPageParamResolver = validPageParamResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(validPageParamResolver);
        resolvers.add(currentUserArgumentResolver);
    }
}
