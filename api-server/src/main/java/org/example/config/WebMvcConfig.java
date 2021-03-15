package org.example.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration.
 */
@Configuration
@ComponentScan(basePackages = {
        "org.example.controller",
        "org.example.services",
        "org.example.web"})
public class WebMvcConfig implements WebMvcConfigurer {
    // Nothing to do
}
