package com.amintech.rag.client;

import com.amintech.rag.dto.Trecho;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "rag.provider", havingValue = "simulado", matchIfMissing = true)
public class SimuladoLlmClient implements LlmClient {

    @Override
    public String gerar(String sistema, String usuario, List<Trecho> trechos) {
        String melhor = trechos.get(0).texto();
        if (melhor.length() > 350) {
            melhor = melhor.substring(0, 350) + "...";
        }
        return "[Resposta simulada] Achei " + trechos.size()
                + " trecho(s) nos documentos. O mais parecido diz: " + melhor;
    }

    @Override
    public String nomeProvedor() {
        return "simulado";
    }

    @Override
    public String nomeModelo() {
        return "nenhum (simulado)";
    }
}
