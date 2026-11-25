package com.amintech.rag.client;

import com.amintech.rag.dto.Trecho;
import java.util.List;

public interface LlmClient {
    String gerar(String sistema, String usuario, List<Trecho> trechos);

    String nomeProvedor();

    String nomeModelo();
}
