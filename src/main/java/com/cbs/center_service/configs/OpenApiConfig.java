package com.cbs.center_service.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.List;

@Configuration
public class OpenApiConfig {

  @Value("${server.servlet.context-path}")
  public String contextPath;

  @Value("${spring.application.name}")
  private String applicationName;

  @Autowired
  private Environment environment;

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Center Service API")
            .version("1.0.0")
            .description("API documentation for Center Service")
            .contact(new Contact()
                .name("CBS Team")
                .email("Por@gmail.com")
                .url("www.pordev.com")

            )
            .license(new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0")
            )
        )
        .servers(List.of(new Server()
            .url(contextPath)
            .description(String.format("Current %s environment", environment.getActiveProfiles()))
        ));

  }
}
