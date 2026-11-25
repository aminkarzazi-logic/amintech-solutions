package com.amintech.rag.dto;

import java.util.List;

public record RespostaResponse(String resposta, String provedor, String modelo, List<Trecho> fontes) {
}
