package com.amintech.springavancada.controller;

import com.amintech.springavancada.exception.ProdutoNaoEncontradoException;
import com.amintech.springavancada.model.Produto;
import com.amintech.springavancada.repository.ProdutoRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoRepository repository;

    // O Spring entrega o repository pronto (injeção de dependência)
    public ProdutoController(ProdutoRepository repository) {
        this.repository = repository;
    }

    // READ (lista), com busca opcional: GET /produtos?nome=mouse
    @GetMapping
    public List<Produto> listar(@RequestParam(required = false) String nome) {
        if (nome == null || nome.isBlank()) {
            return repository.findAll();
        }
        return repository.findByNomeContainingIgnoreCase(nome);
    }

    // Usa a regra de negócio da classe Produto: soma preço x quantidade de todos
    @GetMapping("/valor-total")
    public Map<String, Double> valorTotalDoEstoque() {
        double total = repository.findAll().stream()
                .mapToDouble(Produto::calcularValorTotal)
                .sum();
        return Map.of("valorTotalEstoque", total);
    }

    // READ (um produto): 200, ou 404 se o id não existir
    @GetMapping("/{id}")
    public Produto buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
    }

    // CREATE: 201 Created. O @Valid confere nome, preço e quantidade antes de entrar aqui.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produto criar(@Valid @RequestBody Produto dados) {
        // Cria um produto novo com os dados recebidos (ignora qualquer id enviado)
        Produto novo = new Produto(dados.getNome(), dados.getPreco(), dados.getQuantidade());
        return repository.save(novo);
    }

    // UPDATE: troca os dados de um produto que já existe
    @PutMapping("/{id}")
    public Produto atualizar(@PathVariable Long id, @Valid @RequestBody Produto dados) {
        Produto existente = repository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
        existente.setNome(dados.getNome());
        existente.setPreco(dados.getPreco());
        existente.setQuantidade(dados.getQuantidade());
        return repository.save(existente);
    }

    // DELETE: 204 No Content (deu certo, sem corpo na resposta), ou 404
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void apagar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new ProdutoNaoEncontradoException(id);
        }
        repository.deleteById(id);
    }
}
