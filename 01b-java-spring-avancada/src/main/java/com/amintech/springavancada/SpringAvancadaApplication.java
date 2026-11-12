package com.amintech.springavancada;

import com.amintech.springavancada.model.Produto;
import com.amintech.springavancada.repository.ProdutoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringAvancadaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAvancadaApplication.class, args);
    }

    // Roda uma vez quando o programa liga: coloca 3 produtos de exemplo no banco.
    @Bean
    CommandLineRunner carregarDadosIniciais(ProdutoRepository repository) {
        return args -> {
            repository.save(new Produto("Teclado mecânico", 250.00, 10));
            repository.save(new Produto("Monitor 24 polegadas", 800.00, 5));
            repository.save(new Produto("Mouse sem fio", 90.00, 20));
        };
    }
}
