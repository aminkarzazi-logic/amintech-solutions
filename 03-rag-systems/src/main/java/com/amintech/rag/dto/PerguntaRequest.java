package com.amintech.rag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PerguntaRequest(
        @NotBlank(message = "A pergunta não pode ficar vazia")
        @Size(max = 500, message = "A pergunta pode ter no máximo 500 letras")
        String pergunta) {
}
