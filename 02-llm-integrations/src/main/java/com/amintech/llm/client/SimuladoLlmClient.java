package com.amintech.llm.client;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

// Cliente de mentira: não chama nenhuma API, então não precisa de chave e não gera custo.
// Fica ativo quando llm.provider=simulado (ou quando a propriedade não existe).
@Component
@ConditionalOnProperty(name = "llm.provider", havingValue = "simulado", matchIfMissing = true)
public class SimuladoLlmClient implements LlmClient {

    @Override
    public String gerarResposta(String pergunta) {
        return "[Resposta simulada] Você perguntou: \"" + pergunta + "\". "
                + "Para usar um modelo de IA de verdade, defina LLM_PROVIDER=anthropic e a chave ANTHROPIC_API_KEY.";
    }

    @Override
    public String provedor() {
        return "simulado";
    }

    @Override
    public String modelo() {
        return "nenhum (simulado)";
    }
}

