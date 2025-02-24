Desafio - Outsera - Golden Raspberry Awards

Este projeto é uma API RESTful para gerenciar filmes e calcular intervalos de premiação com base em dados de filmes vencedores.

Antes de começar, certifique-se de ter instalado:

    Java 17 (ou superior)

    Maven (para gerenciamento de dependências e build)

    Git (para clonar o repositório)

    IDE (recomendamos IntelliJ IDEA ou Eclipse)

Como Baixar o Projeto
    
    Clone o repositório usando o comando:
    git clone https://github.com/rmmonteir/Desafio-Filme-Outsera.git

Como Abrir o Projeto

    Abra a IDE de sua preferência (IntelliJ IDEA, Eclipse, etc.).

    Navegue até o diretório onde o projeto foi clonado e selecione.

    Aguarde a IDE importar as dependências do Maven e basta executar o projeto na IDE.

    O csv já está anexo no projeto, quando iniciar a aplicação, os dados serão carregados para o banco H2.

    A API estará disponível em: http://localhost:8080.

Endpoints da API

Aqui estão os principais endpoints disponíveis:

    Listar todos os filmes:
    GET /movie

    Buscar filme por ID:
    GET /movie/{id}

    Criar um novo filme:
    POST /movie

    Atualizar um filme existente:
    PUT /movie/{id}

    Excluir um filme:
    DELETE /movie/{id}

    Importar dados de filmes via CSV:
    POST /movie/importar-csv

    Calcular intervalos de premiação:
    GET /movie/intervalo-premiacao

Como Executar os Testes

O projeto inclui testes de integração. Para executar todos os testes, siga os passos abaixo:

    Execute os testes com o Maven:
    mvn test

Os testes de integração também estão na pasta src/test/java e verificam a integração entre os componentes, incluindo o banco de dados em memória (H2).

Banco de Dados

O projeto usa um banco de dados em memória H2 para testes e desenvolvimento. Você pode acessar o console do H2 durante a execução do projeto no navegador em:

    http://localhost:8080/h2-console

    JDBC URL: jdbc:h2:mem:testdb

    Username: sa

    Password: 

Por fim, caso queira a collection do postman segue o link para baixar:
https://1drv.ms/u/c/55364bc2c047036c/ERYlIUyUTdNNlL1YUbhNOtIBT8jfM4KuEzBu0CbJNlDzSA?e=0P0uGI
    
