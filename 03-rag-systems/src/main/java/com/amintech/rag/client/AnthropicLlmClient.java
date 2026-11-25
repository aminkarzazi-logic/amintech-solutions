package com.amintech.rag.client;

import com.amintech.rag.config.RagProperties;
import com.amintech.rag.dto.Trecho;
import com.amintech.rag.exception.LlmException;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.Map;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
@ConditionalOnProperty(name = "rag.provider", havingValue = "anthropic")
public class AnthropicLlmClient implements LlmClient {

    private final RestClient http;
    private final RagProperties props;

    public AnthropicLlmClient(RagProperties props, RestClient.Builder builder) {
        if (props.apiKey().isBlank()) {
            throw new IllegalStateException("ANTHROPIC_API_KEY não foi definida");
        }
        this.props = props;
        this.http = builder
                .baseUrl("https://api.anthropic.com")
                .defaultHeader("x-api-key", props.apiKey())
                .defaultHeader("anthropic-version", "2023-06-01")
                .build();
    }

    @Override
    public String gerar(String sistema, String usuario, List<Trecho> trechos) {
        Map<String, Object> corpo = Map.of(
                "model", props.model(),
                "max_tokens", 600,
                "system", sistema,
                "messages", List.of(Map.of("role", "user", "content", usuario)));
        try {
            JsonNode json = http.post()
                    .uri("/v1/messages")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(corpo)
                    .retrieve()
                    .body(JsonNode.class);
            String texto = json == null ? "" : json.path("content").path(0).path("text").asText("");
            if (texto.isBlank()) {
                throw new LlmException("O modelo devolveu uma resposta vazia");
            }
            return texto;
        } catch (RestClientException e) {
            throw new LlmException("Problema ao chamar o modelo de IA: " + e.getMessage(), e);
        }
    }

    @Override
    public String nomeProvedor() {
        return "anthropic";
    }

    @Override
    public String nomeModelo() {
        return props.model();
    }
}
