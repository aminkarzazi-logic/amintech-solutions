package com.amintech.llm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

// Reúne as configurações que começam com "llm." no application.properties.
// O Spring preenche este record sozinho.
@ConfigurationProperties(prefix = "llm")
public record LlmProperties(String provider, String model, int maxTokens, String apiKey) {
}
