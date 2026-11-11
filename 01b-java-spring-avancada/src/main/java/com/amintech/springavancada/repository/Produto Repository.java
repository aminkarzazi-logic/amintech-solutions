package com.amintech.springavancada.repository;

import com.amintech.springavancada.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Só de estender JpaRepository, a interface ganha save, findAll, findById,
// existsById, deleteById e outros métodos, sem escrever nenhum SQL.
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // O Spring cria a busca só pelo nome do método:
    // produtos cujo nome contém o texto, sem diferenciar maiúscula de minúscula.
    List<Produto> findByNomeContainingIgnoreCase(String nome);
}
