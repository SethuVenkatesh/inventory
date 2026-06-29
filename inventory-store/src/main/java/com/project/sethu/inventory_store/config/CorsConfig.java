package com.project.sethu.inventory_store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOrigin("http://localhost:4200");
        config.addAllowedOrigin("https://inventory-store-9g45.onrender.com");   // Angular dev server
        config.addAllowedOrigin("https://inventory-store-app.vercel.app");
        config.addAllowedMethod("*");                       // GET, POST, PUT, DELETE, OPTIONS
        config.addAllowedHeader("*");                       // Authorization, Content-Type, etc
        config.setAllowCredentials(true);                   // Allow cookies/auth headers

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);   // Apply to all endpoints

        return new CorsFilter(source);
    }
}
