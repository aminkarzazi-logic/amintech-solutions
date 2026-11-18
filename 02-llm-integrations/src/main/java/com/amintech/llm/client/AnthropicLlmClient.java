package com.amintech.llm.client;

import com.amintech.llm.config.LlmProperties;
import com.amintech.llm.exception.LlmException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Map;

// Cliente de verdade: chama a API de mensagens da Anthropic.
// Só é criado quando llm.provider=anthropic.
@Component
@ConditionalOnProperty(name = "llm.provider", havingValue = "anthropic")
public class AnthropicLlmClient implements LlmClient {

    private static final String BASE_URL = "https://api.anthropic.com";
    private static final String INSTRUCAO_DO_SISTEMA =
            "Você é um assistente que responde em português do Brasil, de forma clara e curta.";

    private final RestClient restClient;
    private final LlmProperties props;

    public AnthropicLlmClient(RestClient.Builder builder, LlmProperties props) {
        this.props = props;
        this.restClient = builder
                .baseUrl(BASE_URL)
                .defaultHeader("anthropic-version", "2023-06-01")
                .build();
    }

    @Override
    public String gerarResposta(String pergunta) {
        if (props.apiKey() == null || props.apiKey().isBlank()) {
            throw new LlmException("Chave de API não configurada. Defina a variável de ambiente ANTHROPIC_API_KEY.");
        }

        // Corpo do pedido: modelo, limite de tokens, instrução do sistema e a mensagem do usuário
        Map<String, Object> corpo = Map.of(
                "model", props.model(),
                "max_tokens", props.maxTokens(),
                "system", INSTRUCAO_DO_SISTEMA,
                "messages", List.of(Map.of("role", "user", "content", pergunta))
        );

        try {
            JsonNode resposta = restClient.post()
                    .uri("/v1/messages")
                    .header("x-api-key", props.apiKey())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(corpo)
                    .retrieve()
                    .body(JsonNode.class);

            // A resposta traz uma lista "content". O texto está no primeiro item.
            String texto = resposta == null ? "" : resposta.path("content").path(0).path("text").asText("");
            if (texto.isBlank()) {
                throw new LlmException("A API respondeu sem texto.");
            }
            return texto;

        } catch (RestClientResponseException e) {
            // A API respondeu, mas com erro (chave errada, modelo inexistente, limite de uso...)
            throw new LlmException("A API respondeu com erro " + e.getStatusCode().value()
                    + ": " + e.getResponseBodyAsString());
        } catch (RestClientException e) {
            // Não conseguiu nem falar com a API (sem internet, tempo esgotado...)
            throw new LlmException("Não foi possível falar com a API: " + e.getMessage());
        }
    }

    @Override
    public String provedor() {
        return "anthropic";
    }

    @Override
    public String modelo() {
        return props.model();
    }
}
