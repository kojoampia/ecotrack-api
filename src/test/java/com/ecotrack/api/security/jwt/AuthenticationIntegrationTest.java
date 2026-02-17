package com.ecotrack.api.security.jwt;

import com.ecotrack.api.config.SecurityConfiguration;
import com.ecotrack.api.config.SecurityJwtConfiguration;
import com.ecotrack.api.config.WebConfigurer;
import com.ecotrack.api.management.SecurityMetersService;
import com.ecotrack.api.web.rest.AuthenticateController;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import tech.ecopster.config.JHipsterProperties;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    properties = {
        "ecopster.security.authentication.jwt.base64-secret=fd54a45s65fds737b9aafcb3412e07ed99b267f33413274720ddbb7f6c5e64e9f14075f2d7ed041592f0b7657baf8",
        "ecopster.security.authentication.jwt.token-validity-in-seconds=60000",
    },
    classes = {
        JHipsterProperties.class,
        WebConfigurer.class,
        SecurityConfiguration.class,
        SecurityJwtConfiguration.class,
        SecurityMetersService.class,
        AuthenticateController.class,
        JwtAuthenticationTestUtils.class,
    }
)
public @interface AuthenticationIntegrationTest {
}
