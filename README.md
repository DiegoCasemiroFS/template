# Template

Projeto base para iniciar novos sistemas com Spring Boot. Ja vem com usuario,
seguranca JWT (access e refresh token), CRUD completo e documentacao pronta, para
que todos os projetos partam da mesma fundacao. Usa PostgreSQL como banco de dados.

## Tecnologias

- Java 21
- Spring Boot 3.5.15 (Web, Data JPA, Security, Validation, Actuator)
- PostgreSQL
- Liquibase (migrations em YAML)
- JWT via jjwt (autenticacao stateless)
- ModelMapper (conversao DTO/entidade)
- Lombok
- springdoc-openapi (Swagger UI)
- JUnit 5 + Mockito (testes de unidade)

## Pre-requisitos

- JDK 21
- PostgreSQL rodando

Crie o banco e um usuario no Postgres, por exemplo:

```sql
CREATE DATABASE template;
CREATE USER template WITH PASSWORD 'template';
GRANT ALL PRIVILEGES ON DATABASE template TO template;
```

## Configuracao (.env)

As credenciais ficam num arquivo `.env` na raiz, que NAO vai para o repositorio
(esta no `.gitignore`). Ha um `.env.example` versionado como modelo. Copie e ajuste:

```bash
cp .env.example .env
```

O `application.properties` le esse arquivo automaticamente via
`spring.config.import=optional:file:.env[.properties]`, entao os valores nao ficam
expostos no codigo. Variaveis esperadas:

- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`: conexao com o Postgres
- `JWT_SECRET`: chave de assinatura do JWT (minimo 32 caracteres)

## Como rodar

```bash
./mvnw spring-boot:run
```

- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html

Na primeira execucao o Liquibase cria a tabela `usuario`.

## Autenticacao

1. `POST /api/auth/register` cria um usuario (perfil USER).
2. `POST /api/auth/login` devolve access token e refresh token.
3. Use o access token nas rotas protegidas: `Authorization: Bearer <token>`.
4. `POST /api/auth/refresh` troca o refresh token por um novo par de tokens.

## Endpoints de usuario

- `GET /api/usuarios/me`: dados do usuario logado
- `GET /api/usuarios/{id}`: busca por id
- `GET /api/usuarios`: listagem paginada (ADMIN)
- `POST /api/usuarios`: cria usuario (ADMIN)
- `PUT /api/usuarios/{id}`: atualiza (ADMIN)
- `DELETE /api/usuarios/{id}`: inativa (ADMIN)

## Estrutura

Organizacao por camada, sob o pacote `api.com.template`:

- `config/`: beans de configuracao (seguranca, OpenAPI, ModelMapper)
- `controller/`: endpoints REST
- `domain/`: entidades, enums e DTOs (input/response)
- `exception/`: excecoes e handler global (devolve ApiError)
- `repository/`: Spring Data JPA
- `security/`: JWT, filtro e UserDetails
- `service/`: regras de negocio

## Testes

Somente testes de unidade com Mockito; nao sobem banco. Os dados de teste sao
montados na classe `TestFixtures`.

```bash
./mvnw test
```

## Producao

Rode com o profile `prod` e forneca as variaveis por ambiente real (sem `.env`):

```bash
java -jar target/template-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```
