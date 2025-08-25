package com.BookStore.AuthenticationService.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "file:${user.dir}/.env", ignoreResourceNotFound = true)
public class EnvConfig {
}
