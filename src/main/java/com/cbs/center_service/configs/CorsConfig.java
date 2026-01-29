package com.cbs.center_service.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
@Slf4j
public class CorsConfig implements WebMvcConfigurer {

  @Value("${cors.allowed-origins:*}")
  private String[] allowedOrigins;

  @Value("${cors.allowed-methods}")
  private String[] allowedMethods;

  @Value("${cors.allowed-headers:*}")
  private String[] allowedHeaders;

  @Value("${cors.exposed-headers}")
  private String[] exposeHeaders;

  @Value("${cors.allowed-credentials}")
  private boolean allowedCredentials;

  @Value("${cors.max-age:3600}")
  private long maxAge;


  @Override
  public void addCorsMappings(CorsRegistry registry) {
    log.info("Configuration CORS with allowed origins: {} ", Arrays.toString(allowedOrigins));
    registry.addMapping("/**")
        .allowedOrigins(allowedOrigins)
        .allowedMethods(allowedMethods)
        .allowedHeaders(allowedHeaders)
        .exposedHeaders(exposeHeaders)
        .allowCredentials(allowedCredentials)
        .maxAge(maxAge);
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    if (allowedOrigins.length == 1 && "*".equals(allowedOrigins[0])) {
      configuration.addAllowedOriginPattern("*");
      log.warn("CORS configured to allow ALL origins (*). This should not be used in production!");
    } else {
      configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));
    }

    configuration.setAllowedMethods(Arrays.asList(allowedMethods));

    if (allowedHeaders.length == 1 && "*".equals(allowedHeaders[0])) {
      configuration.addAllowedHeader("*");
    } else {
      configuration.setAllowedHeaders(Arrays.asList(allowedHeaders));
    }

    if (exposeHeaders.length > 0) {
      configuration.setExposedHeaders(Arrays.asList(exposeHeaders));
    }

    configuration.setAllowCredentials(allowedCredentials);

    configuration.setMaxAge(maxAge);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }

//  @Bean
//  public CorsFilter corsFilter() {
//    return new CorsFilter();
//  }
}
