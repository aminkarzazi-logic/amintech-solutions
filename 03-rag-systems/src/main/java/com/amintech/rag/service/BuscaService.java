package com.amintech.rag.service;

import com.amintech.rag.dto.Trecho;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class BuscaService {

    private static final Set<String> PALAVRAS_COMUNS = Set.of(
            "que", "com", "uma", "uns", "para", "por", "como", "dos", "das", "nos", "nas",
            "mais", "sao", "ele", "ela", "isso", "esse", "essa", "este", "esta", "ser",
            "tem", "seu", "sua", "qual", "quais", "onde", "quando", "quem");

    private final List<Trecho> trechos;
    private final List<Map<String, Integer>> frequencias = new ArrayList<>();
    private final Map<String, Integer> trechosComPalavra = new HashMap<>();

    public BuscaService(DocumentoService documentos) {
        this.trechos = documentos.trechos();
        for (Trecho trecho : trechos) {
            Map<String, Integer> contagem = new HashMap<>();
            for (String palavra : palavras(trecho.texto())) {
                contagem.merge(palavra, 1, Integer::sum);
            }
            frequencias.add(contagem);
            contagem.keySet().forEach(p -> trechosComPalavra.merge(p, 1, Integer::sum));
        }
    }

    public List<Trecho> buscar(String pergunta, int topK) {
        List<String> termos = palavras(pergunta);
        List<Trecho> resultado = new ArrayList<>();
        for (int i = 0; i < trechos.size(); i++) {
            double nota = 0;
            for (String termo : termos) {
                int vezes = frequencias.get(i).getOrDefault(termo, 0);
                if (vezes == 0) {
                    continue;
                }
                // Palavra rara vale mais do que palavra que aparece em tudo
                double raridade = Math.log(1 + (double) trechos.size() / trechosComPalavra.get(termo));
                nota += (1 + Math.log(vezes)) * raridade;
            }
            if (nota > 0) {
                Trecho t = trechos.get(i);
                resultado.add(new Trecho(t.documento(), t.posicao(), t.texto(),
                        Math.round(nota * 100) / 100.0));
            }
        }
        resultado.sort(Comparator.comparingDouble(Trecho::pontuacao).reversed());
        return resultado.stream().limit(topK).toList();
    }

    // Minúsculas, sem acento, sem palavras comuns e sem "s" no final (plural simples)
    static List<String> palavras(String texto) {
        String limpo = Normalizer.normalize(texto.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        List<String> lista = new ArrayList<>();
        for (String p : limpo.split("[^a-z0-9]+")) {
            if (p.length() < 3 || PALAVRAS_COMUNS.contains(p)) {
                continue;
            }
            lista.add(p.length() > 3 && p.endsWith("s") ? p.substring(0, p.length() - 1) : p);
        }
        return lista;
    }
}
