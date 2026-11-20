package com.amintech.llm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Os testes sempre usam o modo simulado: não chamam a API e não custam nada.
@SpringBootTest(properties = "llm.provider=simulado")
@AutoConfigureMockMvc
class LlmControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void perguntaValidaDevolveRespostaSimulada() throws Exception {
        mockMvc.perform(post("/perguntar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"pergunta\":\"O que e Spring Boot?\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.provedor").value("simulado"))
                .andExpect(jsonPath("$.resposta").isNotEmpty());
    }

    @Test
    void perguntaEmBrancoDevolve400() throws Exception {
        mockMvc.perform(post("/perguntar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"pergunta\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.pergunta").exists());
    }

    @Test
    void statusMostraOProvedor() throws Exception {
        mockMvc.perform(get("/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.provedor").value("simulado"));
    }
}
