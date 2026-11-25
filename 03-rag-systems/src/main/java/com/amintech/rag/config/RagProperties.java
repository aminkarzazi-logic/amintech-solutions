package com.amintech.rag.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "rag")
public record RagProperties(
        @DefaultValue("simulado") String provider,
        @DefaultValue("") String apiKey,
        @DefaultValue("claude-sonnet-5-5") String model,
        @DefaultValue("600") int chunkSize,
        @DefaultValue("3") int topK) {
}
