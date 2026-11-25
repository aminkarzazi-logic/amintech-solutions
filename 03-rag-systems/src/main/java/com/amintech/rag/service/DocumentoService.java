package com.amintech.rag.service;

import com.amintech.rag.config.RagProperties;
import com.amintech.rag.dto.Trecho;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

@Service
public class DocumentoService {

    private final List<Trecho> trechos = new ArrayList<>();
    private final Map<String, Integer> trechosPorDocumento = new TreeMap<>();

    public DocumentoService(RagProperties props) {
        try {
            Resource[] arquivos = new PathMatchingResourcePatternResolver()
                    .getResources("classpath:docs/*.md");
            for (Resource arquivo : arquivos) {
                String texto = arquivo.getContentAsString(StandardCharsets.UTF_8);
                dividir(arquivo.getFilename(), texto, props.chunkSize());
            }
        } catch (IOException e) {
            throw new IllegalStateException("Não consegui ler os documentos", e);
        }
    }

    // Junta parágrafos até chegar no tamanho máximo; depois começa um novo trecho
    private void dividir(String documento, String texto, int limite) {
        StringBuilder atual = new StringBuilder();
        int posicao = 0;
        for (String paragrafo : texto.split("\\R\\s*\\R")) {
            paragrafo = paragrafo.strip();
            if (paragrafo.isEmpty()) {
                continue;
            }
            if (atual.length() > 0 && atual.length() + paragrafo.length() > limite) {
                posicao++;
                trechos.add(new Trecho(documento, posicao, atual.toString(), 0));
                atual.setLength(0);
            }
            if (atual.length() > 0) {
                atual.append("\n\n");
            }
            atual.append(paragrafo);
        }
        if (atual.length() > 0) {
            posicao++;
            trechos.add(new Trecho(documento, posicao, atual.toString(), 0));
        }
        trechosPorDocumento.put(documento, posicao);
    }

    public List<Trecho> trechos() {
        return trechos;
    }

    public Map<String, Integer> documentos() {
        return trechosPorDocumento;
    }
}
