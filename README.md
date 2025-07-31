# Sistema de Estoque API

API RESTful completa para gerenciamento de estoque, desenvolvida com Java e o ecossistema Spring. O projeto foi construído seguindo as melhores práticas de arquitetura em camadas (Controller, Service, Repository) e oferece uma base sólida para sistemas de controle de inventário.

## Visão Geral do Projeto

O objetivo deste projeto é fornecer um backend robusto para controlar produtos, suas quantidades em estoque, o histórico de movimentações (entradas e saídas) e a localização física de cada item em um armazém, utilizando um sistema de prateleiras e coordenadas.

---

### Funcionalidades Implementadas

*   **Gerenciamento de Produtos:**
    *   Criação de produtos individualmente ou em lote.
    *   Listagem, busca e exclusão de produtos.
    *   Validação para evitar nomes de produtos duplicados.

*   **Controle de Estoque:**
    *   Registro de transações de entrada para aumentar o estoque de um produto.
    *   Registro de transações de saída, com validação para não permitir estoque negativo.
    *   Todo o histórico de movimentações é salvo para auditoria.

*   **Localização Física:**
    *   Criação de prateleiras (estantes) com códigos únicos.
    *   Alocação de produtos em uma localização específica (prateleira, linha e coluna).
    *   Consulta de todas as localizações de um determinado produto.

*   **Documentação da API:**
    *   A API é totalmente documentada e testável através do Swagger UI, acessível em `/swagger-ui.html`.

---

### Arquitetura e Tecnologias

| Camada / Tecnologia | Descrição                                                              |
| ------------------- | ---------------------------------------------------------------------- |
| **Linguagem**       | Java 17                                                                |
| **Framework**       | Spring Boot 3                                                          |
| **Persistência**    | Spring Data JPA / Hibernate                                            |
| **Banco de Dados**  | PostgreSQL (rodando em um contêiner Docker)                            |
| **Build Tool**      | Apache Maven                                                           |
| **API**             | Spring Web (RESTful)                                                   |
| **Documentação**    | Springdoc OpenAPI (Swagger UI)                                         |
| **Utilitários**     | Lombok para redução de código boilerplate                              |
| **Containerização** | Docker Compose para o ambiente de banco de dados                       |

---

### Como Executar o Projeto

1.  **Pré-requisitos:**
    *   JDK 17 ou superior
    *   Maven 3.8+
    *   Docker e Docker Compose

2.  **Clone o repositório:**
    ```bash
    git clone https://github.com/seu-usuario/sistema-de-estoque-api.git
    cd sistema-de-estoque-api
    ```

3.  **Inicie o banco de dados:**
    ```bash
    docker-compose up -d
    ```

4.  **Execute a aplicação:**
    ```bash
    mvn spring-boot:run
    ```

5.  **Acesse a API:**
    *   A aplicação estará disponível em `http://localhost:8080`.
    *   A documentação do Swagger estará em `http://localhost:8080/swagger-ui.html`.

---
