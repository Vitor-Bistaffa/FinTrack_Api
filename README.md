# FinTrack_Api

A **FinTrack API** é uma aplicação REST desenvolvida com **Spring Boot** que permite o controle de finanças pessoais. Com ela, o usuário pode registrar receitas, despesas e acompanhar o saldo disponível com facilidade.


```mermaid
classDiagram
    class categoria {
        +serial id
        +varchar nome
    }
    class conta {
        +serial id
        +varchar nome
    }
        class tipo_transacao {
        Receita
        Despesa
    }
    <<enumeration>> tipo_transacao
    class transacao {
        +serial id
        +integer fk_id_conta
        +integer fk_id_categoria
        +decimal valor
        +varchar nome
        +text descricao
        +tipo_transacao tipo
        +date data
        +integer parcela
    }
    class saldo {
        +serial id
        +decimal saldo_atual
        +date data
    }
    conta "1" --> "N" transacao : possui
    categoria "1" --> "N" transacao : classifica
```

### Tecnologias Utilizadas
* Java 21
* Spring Boot
* Spring Data JPA
* PostgreSQL
* Docker
* Maven
* Swagger