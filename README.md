# ReadMatch API

**Encontre sua próxima leitura aqui**

Uma API REST para gerenciamento de livros, leituras e recomendações personalizadas baseadas em preferências de gêneros literários.

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Pré-requisitos](#pré-requisitos)
- [Instalação e Execução](#instalação-e-execução)
- [Documentação da API](#documentação-da-api)
- [Estrutura do Banco de Dados](#estrutura-do-banco-de-dados)
- [Exemplos de Uso](#exemplos-de-uso)

## 🎯 Sobre o Projeto

ReadMatch é uma API desenvolvida em Spring Boot que permite:

- **Gerenciar livros**: Adicionar livros através da integração com Google Books API
- **Controlar leituras**: Acompanhar o progresso de leitura com status e avaliações
- **Receber recomendações**: Sistema inteligente de recomendações baseado em preferências de gêneros
- **Definir preferências**: Configurar gêneros favoritos para personalizar recomendações

## 🚀 Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.5.5**
- **Spring Data JPA**
- **Spring Validation**
- **H2 Database** (desenvolvimento)
- **MySQL** (produção)
- **Flyway** (migração de banco)
- **Lombok**
- **Maven**

## 📋 Pré-requisitos

- Java 21 ou superior
- Maven 3.6+
- MySQL (para produção) ou usar H2 (desenvolvimento)

## 🔧 Instalação e Execução

1. **Clone o repositório**
```bash
git clone <url-do-repositorio>
cd ReadMatch
```

2. **Configure o banco de dados**
   - Para desenvolvimento: A aplicação está configurada para usar H2 em memória
   - Para produção: Configure as variáveis de ambiente do MySQL

3. **Execute a aplicação**
```bash
mvn spring-boot:run
```

4. **Acesse o console H2** (desenvolvimento)
   - URL: http://localhost:8080/h2-console
   - JDBC URL: `jdbc:h2:mem:readmatch`
   - Username: `sa`
   - Password: (vazio)

## 📚 Documentação da API
Documentação Postman: https://documenter.getpostman.com/view/39397377/2sB3Hkqfd2

### Base URL
```
http://localhost:8080/api/v1
```

---

## 📖 Endpoints de Livros

### `POST /books`
Adiciona um novo livro através do Google Books API.

**Body:**
```json
{
  "idGoogle": "string",
  "genres": ["string"]
}
```

**Validações:**
- `idGoogle`: Obrigatório, ID do livro no Google Books
- `genres`: Obrigatório, pelo menos um gênero

**Resposta:** `201 Created`
```json
{
  "id": "uuid",
  "title": "string",
  "author": "string", 
  "description": "string",
  "genres": ["string"],
  "publishedYear": 2024,
  "publisher": "string",
  "pageCount": 300
}
```

### `GET /books/{id}`
Busca um livro específico por ID.

**Resposta:** `200 OK`
```json
{
  "id": "uuid",
  "title": "string",
  "author": "string",
  "description": "string", 
  "genres": ["string"],
  "publishedYear": 2024,
  "publisher": "string",
  "pageCount": 300
}
```

### `GET /books`
Lista todos os livros com filtro opcional por gênero.

**Query Parameters:**
- `genero` (opcional): Filtra livros por gênero específico

**Resposta:** `200 OK`
```json
[
  {
    "id": "uuid",
    "title": "string",
    "author": "string",
    "description": "string",
    "genres": ["string"],
    "publishedYear": 2024,
    "publisher": "string", 
    "pageCount": 300
  }
]
```

### `PATCH /books/{id}`
Atualiza informações de um livro.

**Body:**
```json
{
  "title": "string",
  "author": "string",
  "description": "string",
  "genres": ["string"],
  "publisher": "string"
}
```

**Validações:**
- Todos os campos são obrigatórios
- `genres`: Pelo menos um gênero

**Resposta:** `204 No Content`

### `DELETE /books/{id}`
Remove um livro.

**Resposta:** `204 No Content`

---

## 📚 Endpoints de Leituras

### `PATCH /readings/{id}`
Atualiza o progresso de uma leitura.

**Body:**
```json
{
  "startDate": "2024-01-15",
  "endDate": "2024-02-15", 
  "status": "lido",
  "rating": 4.5
}
```

**Validações:**
- `startDate`, `endDate`: Formato `aaaa-mm-dd`
- `status`: Valores permitidos: `"lido"`, `"lendo"`, `"quero ler"`, `"abandonado"`
- `rating`: Entre 0.0 e 5.0

**Resposta:** `204 No Content`

### `GET /readings/{id}`
Busca informações de uma leitura específica.

**Resposta:** `200 OK`
```json
{
  "bookTitle": "string",
  "author": "string",
  "reading": {
    "startDate": "2024-01-15",
    "endDate": "2024-02-15",
    "status": "lido",
    "rating": 4.5
  }
}
```

### `GET /readings`
Lista todas as leituras com filtro opcional por status.

**Query Parameters:**
- `status` (opcional): Filtra por status específico

**Resposta:** `200 OK`
```json
[
  {
    "bookTitle": "string",
    "author": "string", 
    "reading": {
      "startDate": "2024-01-15",
      "endDate": "2024-02-15",
      "status": "lido",
      "rating": 4.5
    }
  }
]
```

---

## 🎯 Endpoints de Preferências e Recomendações

### `POST /preferences`
Define as preferências de gêneros do usuário.

**Body:**
```json
{
  "genres": ["string"]
}
```

**Validações:**
- `genres`: Obrigatório, pelo menos um gênero

**Resposta:** `204 No Content`

### `GET /recommendations`
Obtém recomendações de livros baseadas nas preferências.

**Resposta:** `200 OK`
```json
[
  {
    "idBook": "uuid",
    "bookTitle": "string",
    "author": "string",
    "genreNames": ["string"]
  }
]
```

---

## 🗄️ Estrutura do Banco de Dados

### Tabelas Principais

**tb_books**
- `id` (VARCHAR): ID único do livro
- `title`, `author`, `description`: Informações do livro
- `published_year`, `publisher`, `page_count`: Metadados
- `id_google`: ID no Google Books
- `created_at`: Timestamp de criação

**tb_genres**
- `id` (INT): ID único do gênero
- `name` (VARCHAR): Nome do gênero

**tb_readings**
- `id` (BIGINT): ID único da leitura
- `book_id`: Referência ao livro
- `start_date`, `end_date`: Datas de início e fim
- `status`: Status da leitura (ENUM)
- `rating`: Avaliação (0-5)

**tb_preferences**
- `id` (INT): ID único da preferência
- `genre_id`: Referência ao gênero
- `rating_sum`, `rating_count`, `avg_rating`: Métricas para recomendações

### Relacionamentos
- `tb_books_genres`: Tabela de junção (muitos-para-muitos)
- Restrições de integridade referencial com CASCADE

---

## 💡 Exemplos de Uso

### 1. Adicionar um livro do Google Books
```bash
curl -X POST http://localhost:8080/api/v1/books \
  -H "Content-Type: application/json" \
  -d '{
    "idGoogle": "zyTCAlFPjgYC",
    "genres": ["Ficção", "Romance"]
  }'
```

### 2. Atualizar status de leitura
```bash
curl -X PATCH http://localhost:8080/api/v1/readings/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "startDate": "2024-01-15",
    "endDate": "2024-02-15",
    "status": "lido",
    "rating": 4.5
  }'
```

### 3. Definir preferências
```bash
curl -X POST http://localhost:8080/api/v1/preferences \
  -H "Content-Type: application/json" \
  -d '{
    "genres": ["Ficção Científica", "Fantasia", "Mistério"]
  }'
```

### 4. Obter recomendações
```bash
curl -X GET http://localhost:8080/api/v1/recommendations
```

---

## 🔧 Configuração

A aplicação utiliza o arquivo `application.yml` para configuração:

- **Perfil de desenvolvimento**: H2 em memória
- **Integração**: Google Books API
- **Migração**: Flyway para versionamento do banco
- **Validação**: Bean Validation com mensagens personalizadas

---

## 🎯 Funcionalidades Principais

1. **Integração com Google Books**: Importa automaticamente metadados de livros
2. **Sistema de Avaliação**: Controle de progresso e avaliação de leituras  
3. **Recomendações Inteligentes**: Baseadas em preferências e histórico de avaliações
4. **Validação Robusta**: Validações customizadas em todos os endpoints
5. **Banco H2**: Facilita desenvolvimento e testes

---

## 📄 Status Codes

- `200 OK`: Sucesso na consulta
- `201 Created`: Recurso criado com sucesso
- `204 No Content`: Operação realizada sem retorno de dados
- `400 Bad Request`: Erro de validação nos dados enviados
- `404 Not Found`: Recurso não encontrado
- `500 Internal Server Error`: Erro interno do servidor

---

*Desenvolvido com ❤️ usando Spring Boot*
