package com.amintech.llm.exception;

// Erro criado por nós: algo deu errado ao falar com o modelo de IA.
public class LlmException extends RuntimeException {

    public LlmException(String mensagem) {
        super(mensagem);
    }
}
