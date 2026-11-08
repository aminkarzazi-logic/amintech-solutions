Módulo 01b - API REST completa com Spring Boot (CRUD, validação e banco de dados)
Continuação do módulo 01. Aqui a API deixa de só ler dados fixos: ela cria, lê, atualiza e apaga produtos (CRUD), valida os dados recebidos, trata erros com mensagens claras e guarda tudo em um banco de dados H2.
O que este projeto mostra
CRUD completo: os quatro métodos HTTP (`GET`, `POST`, `PUT`, `DELETE`) em `ProdutoController`.
Banco de dados com JPA: `Produto` é uma entidade (`@Entity`) e `ProdutoRepository` salva e busca sem escrever SQL.
Validação: `@NotBlank`, `@Positive` e `@PositiveOrZero` impedem dados inválidos.
Tratamento de erros: `GlobalExceptionHandler` devolve 400 (dados inválidos) e 404 (produto não existe) em JSON.
Painel de teste: uma página HTML em `/` para testar a API pelo navegador.
Estrutura
```
01b-java-spring-avancada/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/com/amintech/springavancada/
    │   │   ├── SpringAvancadaApplication.java
    │   │   ├── controller/ProdutoController.java
    │   │   ├── exception/GlobalExceptionHandler.java
    │   │   ├── exception/ProdutoNaoEncontradoException.java
    │   │   ├── model/Produto.java
    │   │   └── repository/ProdutoRepository.java
    │   └── resources/
    │       ├── application.properties
    │       └── static/index.html
    └── test/java/com/amintech/springavancada/SpringAvancadaApplicationTests.java
```
Como executar
Pré-requisitos: Java 17 ou mais novo e Maven. Dentro desta pasta:
    mvn spring-boot:run

Antes de rodar, pare o módulo 01 (Ctrl + C), porque os dois usam a porta 8080.
Endpoints
Método	Rota	O que faz	Resposta
GET	`/produtos`	Lista os produtos (aceita `?nome=texto`)	200
GET	`/produtos/{id}`	Busca um produto	200 ou 404
POST	`/produtos`	Cria um produto	201 ou 400
PUT	`/produtos/{id}`	Atualiza um produto	200, 400 ou 404
DELETE	`/produtos/{id}`	Apaga um produto	204 ou 404
GET	`/produtos/valor-total`	Soma preço x quantidade de todos os produtos	200
Exemplo de corpo (JSON) para `POST` e `PUT`:
    {"nome": "Webcam HD", "preco": 199.90, "quantidade": 15}

Como testar
Painel de teste: `http://localhost:8080/`
Console do banco: `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:mem:amintech`, usuário `sa`, senha em branco)
Testes automáticos: `mvn test`
Limites (é um projeto de estudo)
O banco H2 fica na memória: os dados voltam ao início toda vez que o programa reinicia.
Não há login nem controle de acesso.
A entidade é usada direto na API (em projetos maiores se usa um DTO), e o preço usa `double` (em sistemas reais, dinheiro usa `BigDecimal`).
O único teste automático confere se o programa liga.
