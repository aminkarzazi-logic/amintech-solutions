# Spring Boot

Spring Boot é uma ferramenta para criar aplicações Java com pouca configuração. Ele já traz um servidor web pronto, então você roda o projeto com um único comando.

Um controller recebe os pedidos HTTP, como POST e GET, e devolve respostas em JSON. A validação serve para recusar pedidos errados, por exemplo uma pergunta vazia, antes de usar o resto do programa.

As configurações ficam no arquivo application.properties. Segredos, como chaves de API, devem vir de variáveis de ambiente e nunca ficar dentro do código.
