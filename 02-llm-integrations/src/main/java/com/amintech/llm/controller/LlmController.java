package com.amintech.llm.controller;

import com.amintech.llm.client.LlmClient;
import com.amintech.llm.config.LlmProperties;
import com.amintech.llm.dto.PerguntaRequest;
import com.amintech.llm.dto.RespostaResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LlmController {

    private final LlmClient llmClient;
    private final LlmProperties props;

    // O Spring entrega o cliente que estiver ativo (simulado ou anthropic)
    public LlmController(LlmClient llmClient, LlmProperties props) {
        this.llmClient = llmClient;
        this.props = props;
    }

    // Recebe uma pergunta e devolve a resposta do modelo
    @PostMapping("/perguntar")
    public RespostaResponse perguntar(@Valid @RequestBody PerguntaRequest pedido) {
        String resposta = llmClient.gerarResposta(pedido.pergunta().trim());
        return new RespostaResponse(resposta, llmClient.provedor(), llmClient.modelo());
    }

    // Mostra como a aplicação está configurada (nunca mostra a chave, só se ela existe)
    @GetMapping("/status")
    public Map<String, Object> status() {
        boolean chaveConfigurada = props.apiKey() != null && !props.apiKey().isBlank();
        return Map.of(
                "provedor", llmClient.provedor(),
                "modelo", llmClient.modelo(),
                "chaveConfigurada", chaveConfigurada
        );
    }
}





