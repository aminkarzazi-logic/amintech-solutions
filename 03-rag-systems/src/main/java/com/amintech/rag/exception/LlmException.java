package com.amintech.rag.exception;

public class LlmException extends RuntimeException {
    public LlmException(String mensagem) {
        super(mensagem);
    }

    public LlmException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
