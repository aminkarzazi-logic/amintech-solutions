package com.amintech.rag.controller;

import com.amintech.rag.client.LlmClient;
import com.amintech.rag.config.RagProperties;
import com.amintech.rag.dto.PerguntaRequest;
import com.amintech.rag.dto.RespostaResponse;
import com.amintech.rag.service.DocumentoService;
import com.amintech.rag.service.RagService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RagController {

    private final RagService rag;
    private final LlmClient llm;
    private final RagProperties props;
    private final DocumentoService documentos;

    public RagController(RagService rag, LlmClient llm, RagProperties props, DocumentoService documentos) {
        this.rag = rag;
        this.llm = llm;
        this.props = props;
        this.documentos = documentos;
    }

    @PostMapping("/perguntar")
    public RespostaResponse perguntar(@Valid @RequestBody PerguntaRequest pedido) {
        return rag.perguntar(pedido.pergunta());
    }

    @GetMapping("/status")
    public Map<String, Object> status() {
        return Map.of(
                "provedor", llm.nomeProvedor(),
                "modelo", llm.nomeModelo(),
                "chaveExiste", !props.apiKey().isBlank(),
                "documentos", documentos.documentos().size(),
                "trechos", documentos.trechos().size());
    }

    @GetMapping("/documentos")
    public Map<String, Integer> listarDocumentos() {
        return documentos.documentos();
    }
}
