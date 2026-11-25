package com.amintech.rag.service;

import com.amintech.rag.client.LlmClient;
import com.amintech.rag.config.RagProperties;
import com.amintech.rag.dto.RespostaResponse;
import com.amintech.rag.dto.Trecho;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RagService {

    private static final String SISTEMA = """
            Você responde perguntas usando SOMENTE o contexto que vem na mensagem.
            Se a resposta não estiver no contexto, diga que não encontrou nos documentos.
            Responda em português simples e curto.""";

    private final BuscaService busca;
    private final LlmClient llm;
    private final RagProperties props;

    public RagService(BuscaService busca, LlmClient llm, RagProperties props) {
        this.busca = busca;
        this.llm = llm;
        this.props = props;
    }

    public RespostaResponse perguntar(String pergunta) {
        List<Trecho> trechos = busca.buscar(pergunta, props.topK());

        // Sem trecho útil, nem chama o modelo (não gasta dinheiro)
        if (trechos.isEmpty()) {
            return new RespostaResponse("Não encontrei nada nos documentos sobre isso.",
                    llm.nomeProvedor(), llm.nomeModelo(), trechos);
        }

        StringBuilder contexto = new StringBuilder();
        for (Trecho t : trechos) {
            contexto.append("[Fonte: ").append(t.documento()).append(" #").append(t.posicao()).append("]\n")
                    .append(t.texto()).append("\n\n");
        }
        String usuario = "Contexto:\n" + contexto + "Pergunta: " + pergunta;

        String resposta = llm.gerar(SISTEMA, usuario, trechos);
        return new RespostaResponse(resposta, llm.nomeProvedor(), llm.nomeModelo(), trechos);
    }
}
