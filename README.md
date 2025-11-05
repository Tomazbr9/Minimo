# LinkShort

**Java • Spring Boot • PostgreSQL • Docker**

**LinkShort** transforma URLs longas em versões curtas e fáceis de compartilhar.  
Além disso, registra métricas básicas, como data de criação e número de acessos, permitindo rastrear a popularidade de cada link.

O **Linkshort** também conta com autenticação baseada em **JWT (JSON Web Tokens)**, garantindo que apenas usuários autenticados possam encurtar URLs, consultar estatísticas e acessar endpoints privados.

---

## Sumário
- [Visão Geral](#visão-geral)
- [Interface Web]()
- [Arquitetura do Projeto](#-arquitetura-do-projeto)
- [Tecnologias e Ferramentas](#-tecnologias-e-ferramentas)
- [Camadas do Sistema](#-camadas-do-sistema)
- [Banco de Dados](#-banco-de-dados)
- [Variáveis de Ambiente](#-variáveis-de-ambiente)
- [Execução do Projeto](#-execução-do-projeto)
- [Documentação da API (Swagger / OpenAPI)](#documentação-da-api-swagger--openapi)
- [Endpoints Principais](#-endpoints-principais)
- [Boas Práticas e Padrões](#-boas-práticas-e-padrões)
- [Testes](#-testes)
- [Contribuição](#-contribuição)
- [Licença](#-licença)
- [Contato](#-contato)

---

## Visão Geral

O **LinkShort** transforma URLs longas em versões curtas e fáceis de compartilhar.  
Além disso, registra métricas básicas, como data de criação e número de acessos, permitindo rastrear a popularidade de cada link.

**Principais funcionalidades:**
- Encurtamento de URLs longas em códigos únicos;
- Redirecionamento automático para a URL original;
- Registro e consulta de estatísticas básicas;
- Validação e tratamento de erros personalizados.

---

## Interface Web (Frontend)

O frontend do **URLShortener** é desenvolvido em **Angular + TypeScript** e está disponível em um repositório separado:

➡️ [**LinkShortUI – Interface Web (Angular)**](https://github.com/Tomazbr9/LinkShortUI)

Este projeto oferece uma experiência moderna, responsiva e integrada ao backend, com:

- 🔐 **Autenticação JWT**
- 🔗 **Criação e gerenciamento de URLs encurtadas**
- 🎨 **Design profissional e responsivo**

---

## Arquitetura do Projeto

A aplicação segue o padrão **arquitetura em camadas**, separando responsabilidades de forma clara:

```bash
LinkShort/
├── src/
│   ├── main/
│   │   ├── java/com/tomazbr9/linkshort/
│   │   │   ├── controller/
│   │   │   ├── config/
│   │   │   ├── security/  
│   │   │   ├── service/
│   │   │   ├── enums/
│   │   │   ├── repository/
│   │   │   ├── model/
│   │   │   ├── dto/
│   │   │   └── exception/
│   │   └── resources/
│   │       ├── application.properties
│   │       
│   └── test/
│       └── ...
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md

```
---
## Tecnologias e Ferramentas

- **Java 17**
- **Spring Boot 3.x**
- **Spring Web**
- **Spring Security**
- **JWT**
- **Spring Data JPA**
- **PostgreSQL**
- **Lombok**
- **Docker & Docker Compose**
- **JUnit 5 / Mockito**

---

## Camadas do Sistema

| Camada         | Responsabilidade                                                                  |
|----------------|-----------------------------------------------------------------------------------|
| **Controller** | Expor endpoints REST e receber requisições HTTP                                   |
| **Service**    | Contém a lógica de negócio, validações e orquestra chamadas às camadas inferiores |
| **Repository** | Comunicação direta com o banco via Spring Data JPA                                |
| **Model**      | Entidades persistidas no banco de dados                                           |
| **DTO**        | Transferência de dados entre camadas e para respostas HTTP                        |
| **Exception**  | Centralização do tratamento de erros e respostas personalizadas                   |
| **Security**   | Configuraçoes de segurança e autenticação (JWT, Filters...)                       |

---

## Banco de Dados

O projeto utiliza **PostgreSQL** como banco principal.  
As URLs são armazenadas com as seguintes informações básicas:

| Campo          | Tipo      | Descrição                           |
|----------------|-----------|-------------------------------------|
| `id`           | UUID      | Identificador único                 |
| `urlName`      | String    | Nome da URL                         |
| `originalUrl`  | String    | URL original fornecida pelo usuário |
| `shortenedUrl` | String    | Código gerado automaticamente       |
| `createdAt`    | LocalDate | Data de criação                     |
| `totalClicks`  | Integer   | Contador de acessos                 |
| `user`         | User      | Usuário vinculado a URL             |

---

## Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto com as seguintes variáveis:

```bash

# Banco de Dados
POSTGRES_DB=linkshort
POSTGRES_USER=postgres
POSTGRES_PASSWORD=minhasenha123

SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-db:5432/linkshort
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=minhasenha123

# Chave secreta JWT
SECRET_KEY=chave-secreta
```

## Configuração do `application.properties`

No arquivo `application.properties`, você pode referenciar as variáveis com:

```properties
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
```

---

## Execução do Projeto

### Pré-requisitos
- Java **17**
- **Maven**
- **Docker e Docker Compose**

---

### Rodar com Maven
```bash
./mvnw spring-boot:run
```

### Rodar com Docker
```bash
docker-compose up --build
```

### Rodar Testes
```bash
./mvnw test
```

---

## Documentação da API (Swagger / OpenAPI)

A documentação da API do **LinkShort** foi gerada com **Swagger** (OpenAPI) e está disponível diretamente no backend.

### Como acessar

1. Certifique-se de que o servidor backend esteja em execução.
2. Acesse a seguinte URL no navegador: http://localhost:8080/swagger-ui/index.html

*(O endereço pode variar conforme a configuração do seu ambiente — ajuste a porta ou o caminho, se necessário.)*

3. A interface do **Swagger UI** permitirá:
- Explorar os endpoints da API
- Testar requisições diretamente pelo navegador
- Visualizar exemplos de respostas e modelos de dados

---

## Endpoints Principais

| Método     | Endpoint        | Descrição                               |
|------------|-----------------|-----------------------------------------|
| **POST**   | `/api/url`      | Encurta uma URL longa                   |
| **GET**    | `/**`           | Redireciona para a URL original         |
| **GET**    | `/api/url`      | Lista todas as URLs registradas         |
| **GET**    | `/api/url/{id}` | Busca informações de uma URL específica |
| **PUT**    | `/api/url/{id}` | Atualiza uma URL do sistema             |
| **DELETE** | `/api/url/{id}` | Remove uma URL do sistema               |


### Exemplo de Encurtamento

**Requisição:**
```json
POST /api/url
{
{
  "urlName": "nome-da-url",
  "originalUrl": "https://urloriginal.com"
}
}
```

**Resposta:**
```json
{
  "id": "7242ba43-926a-4d2e-a9fd-93e53991ee54",
  "urlName": "nome-da-url",
  "totalClicks": 0,
  "shortenedUrl": "3qetNTka",
  "originalUrl": "https://urloriginal.com",
  "createdIn": "2025-11-03"
}
}
```

---

## Boas Práticas e Padrões

- Código limpo e padronizado com **Lombok** e **DTOs**
- Tratamento centralizado de exceções com `@ControllerAdvice`
- Validação com `javax.validation`
- Padrão **Camadas + SOLID + DTO + Exception Handling**
- Logs informativos usando **Slf4j**

---

## Testes

Os testes são feitos com **JUnit 5** e **Mockito**:

- Testes de **service** validam regras de negócio e geração de códigos
- Testes de **controller** garantem as respostas e status HTTP corretos

**Para rodar:**
```bash
./mvnw test
```

---

## Contribuição

Contribuições são bem-vindas! 

1. Faça um **fork** do repositório
2. Crie uma branch para sua feature:
   ```bash
   git checkout -b minha-feature
   ```
3. Faça suas alterações e envie um **Pull Request**

---

## Licença

Este projeto está licenciado sob a licença **MIT**.  
Veja o arquivo `LICENSE` para mais detalhes.

---

## Contato

💼 **Autor:** Bruno Tomaz  
📧 **Email:** brunotomaaz@yahoo.com \
🔗 **LinkedIn:** [https://www.linkedin.com/in/bruno-tomaz-5232451b2/](https://www.linkedin.com/in/bruno-tomaz-5232451b2/)  
📂 **GitHub:** [https://github.com/seuusuario](https://github.com/seuusuario)

