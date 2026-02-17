package com.ecotrack.api.config;

import com.ecotrack.api.aop.logging.LoggingAspect;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import tech.ecopster.config.JHipsterConstants;

@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectConfiguration {

    @Bean
    @Profile(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT)
    public LoggingAspect loggingAspect(Environment env) {
        return new LoggingAspect(env);
    }
}
