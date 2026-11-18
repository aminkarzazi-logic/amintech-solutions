package com.amintech.llm.dto;

// O que a API devolve: a resposta do modelo e de onde ela veio
public record RespostaResponse(String resposta, String provedor, String modelo) {
}
