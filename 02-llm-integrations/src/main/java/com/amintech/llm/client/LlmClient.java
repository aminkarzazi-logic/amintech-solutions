package com.amintech.llm.client;

// O "contrato" para falar com um modelo de IA.
// O controller conhece só esta interface, e não sabe qual modelo está por trás.
public interface LlmClient {

    String gerarResposta(String pergunta);

    String provedor();

    String modelo();
}
