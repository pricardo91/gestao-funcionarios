# 🏢 Sistema de Gestão de Funcionários

API RESTful desenvolvida em Java com Spring Boot para gerenciamento de funcionários. O projeto foi construído com foco em boas práticas de arquitetura, qualidade de código, documentação e tratamento de erros.

---

## 📋 Sumário

- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Arquitetura](#arquitetura)
- [Estrutura de Pacotes](#estrutura-de-pacotes)
- [Endpoints](#endpoints)
- [Como Executar](#como-executar)
- [Documentação da API](#documentação-da-api)
- [Testes](#testes)
- [Decisões Técnicas](#decisões-técnicas)

---

## 📌 Sobre o Projeto

O sistema permite o cadastro, consulta, atualização e exclusão de funcionários. Como banco de dados, utiliza um `ConcurrentHashMap` em memória, garantindo thread-safety para requisições paralelas.

**Funcionalidades:**
- Cadastrar novo funcionário
- Buscar funcionário por ID
- Listar todos os funcionários
- Atualizar dados de um funcionário
- Excluir funcionário (com regra de negócio: Gerentes não podem ser excluídos)

---

## 🛠 Tecnologias Utilizadas

| Tecnologia | Versão | Descrição |
|---|---|---|
| Java | 17 | Linguagem principal |
| Spring Boot | 3.5.10 | Framework principal |
| Spring Web | - | Camada REST |
| Spring Validation | - | Validação de dados |
| Lombok | - | Redução de boilerplate |
| SpringDoc OpenAPI | 2.7.0 | Documentação Swagger |
| JUnit 5 | - | Framework de testes |
| Mockito | - | Mock de dependências |

---

## 🏛 Arquitetura

O projeto segue uma arquitetura em camadas com separação clara de responsabilidades:

```
Cliente HTTP
     ↓
Controller (recebe DTOs de entrada)
     ↓
Mapper (converte DTO → Domínio)
     ↓
Service (regras de negócio)
     ↓
Repository (persistência em Map)
     ↓
Mapper (converte Domínio → DTO de saída)
     ↓
Cliente HTTP (recebe DTO de resposta)
```

**Princípios aplicados:**
- **Single Responsibility Principle** — cada classe tem uma única responsabilidade
- **Separation of Concerns** — domínio nunca é exposto diretamente na API
- **Fail Fast** — validações ocorrem na entrada, antes de processar

---

## 📁 Estrutura de Pacotes

```
br.com.first.gestaofuncionarios
├── api
│   ├── controller
│   │   └── FuncionarioController.java     # Endpoints REST
│   ├── dto
│   │   ├── FuncionarioCreateRequest.java  # DTO de criação
│   │   ├── FuncionarioUpdateRequest.java  # DTO de atualização
│   │   └── FuncionarioResponse.java       # DTO de resposta
│   └── mapper
│       └── FuncionarioMapper.java         # Conversão entre DTO e Domínio
├── config
│   └── OpenApiConfig.java                 # Configuração do Swagger
├── domain
│   └── Funcionario.java                   # Entidade de domínio
├── exception
│   ├── FuncionarioDuplicadoException.java
│   └── FuncionarioNaoEncontradoException.java
├── handler
│   ├── APIException.java                  # Exception base customizada
│   ├── ErrorApiResponse.java              # Corpo padrão de erro
│   └── GlobalExceptionHandler.java        # Interceptador global de erros
├── repository
│   └── FuncionarioRepository.java         # Persistência em ConcurrentHashMap
└── service
    └── FuncionarioService.java            # Regras de negócio
```

---

## 🔗 Endpoints

### Funcionários

| Método | Endpoint | Descrição | Status de Sucesso |
|---|---|---|---|
| `POST` | `/v1/funcionarios` | Cadastrar novo funcionário | `201 Created` |
| `GET` | `/v1/funcionarios` | Listar todos os funcionários | `200 OK` |
| `GET` | `/v1/funcionarios/{id}` | Buscar funcionário por ID | `200 OK` |
| `PUT` | `/v1/funcionarios/{id}` | Atualizar funcionário | `200 OK` |
| `DELETE` | `/v1/funcionarios/{id}` | Excluir funcionário | `204 No Content` |

### Exemplo de Requisição — Criar Funcionário

```http
POST /v1/funcionarios
Content-Type: application/json

{
  "nome": "Ricardo Silva",
  "funcao": "Analista de Sistemas",
  "salario": 7400.50,
  "telefone": "11982971929",
  "endereco": "Rua América Latina, 135 - São Paulo/SP"
}
```

### Exemplo de Resposta — Sucesso

```json
{
  "idFuncionario": 1,
  "nome": "Ricardo Silva",
  "funcao": "Analista de Sistemas",
  "salario": 7400.50,
  "telefone": "11982971929",
  "endereco": "Rua América Latina, 135 - São Paulo/SP"
}
```

### Exemplo de Resposta — Erro

```json
{
  "timestamp": "2026-02-24T13:00:00",
  "status": 409,
  "error": "Conflict",
  "message": "Funcionário já cadastrado",
  "description": "Já existe um funcionário com o telefone: 11982971929"
}
```

---

## ▶️ Como Executar

### Pré-requisitos

- Java 17+
- Maven 3.8+ (ou use o Maven Wrapper incluso)

### Passos

```bash
# Clone o repositório
git clone https://github.com/pricardo91/gestao-funcionarios.git

# Acesse o diretório
cd gestao-funcionarios

# Execute a aplicação
./mvnw spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`

---

## 📖 Documentação da API

Com a aplicação em execução, acesse a documentação interativa do Swagger:

```
http://localhost:8080/swagger-ui/index.html
```

O contrato OpenAPI em formato JSON está disponível em:

```
http://localhost:8080/v3/api-docs
```

---

## 🧪 Testes

O projeto conta com testes unitários da camada de serviço utilizando JUnit 5 e Mockito.

```bash
# Executar todos os testes
./mvnw test
```

**Cenários cobertos:**

| Cenário | Método |
|---|---|
| Deve salvar funcionário com sucesso | `adicionar` - happy path |
| Deve lançar exceção quando telefone já existe | `adicionar` - sad path |

---

## 💡 Decisões Técnicas

**Por que `ConcurrentHashMap`?**
Em um sistema web, requisições podem ocorrer em paralelo. O `HashMap` padrão não é thread-safe e poderia lançar `ConcurrentModificationException`. O `ConcurrentHashMap` resolve isso nativamente.

**Por que `AtomicLong` para geração de ID?**
Garante que dois threads nunca gerem o mesmo ID simultaneamente, usando operações CAS (Compare-And-Swap) do processador. Em um cenário com banco relacional, essa responsabilidade seria delegada ao banco via sequence ou auto-increment.

**Por que DTOs separados para Create e Update?**
Cada operação tem um contrato próprio. Separar os DTOs permite que cada um evolua de forma independente sem impactar o outro, seguindo o Single Responsibility Principle.

**Por que o Service não conhece DTOs?**
A camada de serviço contém as regras de negócio e deve ser independente do protocolo de comunicação. Isso permite reutilizá-la em diferentes contextos sem alteração.

**Por que Exceptions customizadas?**
Exceptions genéricas podem vazar informações internas do sistema. As customizadas permitem controlar exatamente o que é exposto ao usuário — mensagem, status HTTP e nível de detalhe.

---

## 👨‍💻 Autor

**Ricardo Silva**
- Email: pricardo.sribeiro@gmail.com

---

*Projeto desenvolvido como avaliação técnica para F1rst Tecnologia e Inovação.*
