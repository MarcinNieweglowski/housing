package com.marcin.housing.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mail")
public record MailConfig(String to, String text, String subject) {
}
