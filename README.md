# GameStore API - Backend Spring Boot

Plataforma de backend RESTful desenvolvida em Java 17 com Spring Boot 3 para o gerenciamento de uma loja virtual de jogos e carteiras digitais. A aplicação segue padrões de arquitetura em camadas, autenticação stateless baseada em JSON Web Tokens (JWT), persistência com Spring Data JPA em PostgreSQL e tratamento padronizado de erros via ControllerAdvice.

---

## 1. Tecnologias e Dependências

- Java 17 (LTS)
- Spring Boot 3.4
- Spring Security 6 (Configuração Stateless com JWT)
- Spring Data JPA / Hibernate
- Banco de Dados PostgreSQL (Produção/Docker) e H2 Database (Testes de integração)
- Maven (Gerenciador de Dependências e Build)
- Jakarta Bean Validation (Validação de DTOs e requisições)
- Docker e Docker Compose

---

## 2. Arquitetura e Estrutura de Pacotes

A aplicação adota uma arquitetura limpa em camadas (Layered Architecture), separando estritamente responsabilidades de apresentação HTTP, regras de negócio e acesso a dados:

```
br.ufpb.dcx.iago.lojadejogos.backend
├── config
│   ├── CorsConfig.java           # Configuração global de CORS (origens, headers e verbos HTTP liberados)
│   ├── SecurityConfig.java       # Cadeia de filtros de segurança, rotas públicas e autorização por roles
│   └── DataSeeder.java           # Execução automática do script de população inicial (data.sql)
├── controller
│   ├── AuthController.java       # Endpoints de autenticação (/api/v1/auth/login, /api/v1/auth/me)
│   ├── UserController.java       # Gestão de usuários, carteira e biblioteca (/api/v1/usuarios)
│   ├── JogoController.java       # Catálogo e vitrine de jogos (/api/v1/jogos)
│   └── CompraController.java     # Processamento de compras e histórico (/api/v1/compras)
├── dto
│   ├── UserRequestDTO.java       # DTO para criação/edição de conta e carteira
│   ├── UserResponseDTO.java      # DTO de saída sem dados sensíveis (senha ocultada)
│   ├── JogoRequestDTO.java       # DTO para cadastro de jogos no catálogo
│   ├── JogoResponseDTO.java      # DTO de retorno de jogo para a vitrine
│   ├── CompraRequestDTO.java     # DTO contendo user_id e jogo_id para transação
│   └── CompraResponseDTO.java    # DTO com recibo e data/valor da compra efetuada
├── exception
│   ├── GlobalExceptionHandler.java # Tratamento global de exceções (retorno JSON padronizado)
│   ├── SaldoInsuficienteException.java # Exceção de negócio para saldo menor que o preço do jogo
│   ├── JogoJaPossuidoException.java    # Exceção de negócio ao tentar comprar jogo já adquirido
│   ├── EmailJaCadastradoException.java # Exceção em tentativas de cadastro com email duplicado
│   └── UsuarioNaoEncontradoException.java # Exceção quando ID não existe
├── model
│   ├── User.java                 # Entidade JPA de Usuário (com saldo, role e coleção de jogos)
│   ├── Jogo.java                 # Entidade JPA do Jogo (com restrição única de nome)
│   └── Compra.java               # Entidade JPA de registro de transação
├── repository
│   ├── UserRepository.java       # Interface Spring Data JPA para usuários
│   ├── JogoRepository.java       # Interface Spring Data JPA para jogos
│   └── CompraRepository.java     # Interface Spring Data JPA para transações de compra
├── security
│   ├── JwtService.java           # Emissão, verificação de assinatura e parse de tokens JWT
│   └── JwtAuthenticationFilter.java # Filtro que intercepta requisições HTTP e injeta o usuário no SecurityContext
└── service
    ├── UserService.java          # Lógica de negócio de contas, recarga de saldo e listagem de biblioteca
    ├── JogoService.java          # Lógica do catálogo e integração opcional com capas (IGDB/Twitch)
    └── CompraService.java        # Transação atômica de compra e validações financeiras
```

---

## 3. Principais Regras de Negócio e Transações

1. Autenticação e Segurança Stateless:
   - A API não utiliza sessões HTTP em memória. Cada chamada a endpoints protegidos necessita de um header HTTP: `Authorization: Bearer <TOKEN_JWT>`.
   - As senhas dos usuários são armazenadas no banco de dados com hash criptográfico BCrypt.
   - O endpoint `GET /api/v1/auth/me` extrai o email logado diretamente do contexto de segurança da requisição atual e devolve os dados autenticados (incluindo saldo e se é administrador).

2. Transação Atômica de Compra (CompraService):
   - A operação de compra é garantida pelo isolamento `@Transactional`.
   - O serviço realiza três verificações sequenciais:
     1. Existência do usuário e do jogo.
     2. Verificação se o usuário já possui o jogo na sua biblioteca (`JogoJaPossuidoException`).
     3. Verificação de saldo disponível na carteira do usuário (`SaldoInsuficienteException`).
   - Se validado, o valor é subtraído do saldo do usuário, o jogo é adicionado à lista de jogos adquiridos (`usuario_jogos`) e o recibo (`compra`) é gravado.

3. Recarga e Gestão de Carteira:
   - O endpoint `PATCH /api/v1/usuarios/{id}/saldo` (e seu alias via `POST`) recebe o atributo JSON `"valor"` e adiciona o montante ao saldo atual do usuário, impedindo valores negativos ou nulos.

4. Unicidade de Catálogo e Integridade Referencial:
   - A tabela `jogo` possui restrição `UNIQUE(nome)` no banco de dados para evitar cadastros duplicados.
   - O método `deletarJogoPorId` limpa previamente as chaves estrangeiras nas tabelas de junção (`usuario_jogos` e `compra`) antes de remover o jogo, impedindo violações de chave estrangeira ao excluir itens do catálogo.

---

## 4. Endpoints da API

### 4.1. Autenticação (/api/v1/auth)

| Método | Rota | Descrição | Permissão |
|--------|------|-----------|-----------|
| POST | `/api/v1/auth/login` | Realiza autenticação com email e senha, retornando token JWT. | Pública |
| GET | `/api/v1/auth/me` | Retorna o perfil do usuário correspondente ao token enviado no cabeçalho. | Autenticado |

### 4.2. Usuários e Carteira (/api/v1/usuarios)

| Método | Rota | Descrição | Permissão |
|--------|------|-----------|-----------|
| POST | `/api/v1/usuarios` | Cadastra uma nova conta de usuário no sistema. | Pública |
| GET | `/api/v1/usuarios` | Lista todos os usuários cadastrados. | Admin |
| GET | `/api/v1/usuarios/{id}` | Retorna detalhes de um usuário pelo ID. | Autenticado |
| PUT | `/api/v1/usuarios/{id}` | Atualiza nome e email de uma conta. | Autenticado |
| PATCH / POST | `/api/v1/usuarios/{id}/saldo` | Adiciona saldo (R$) à carteira do usuário. | Autenticado |
| DELETE | `/api/v1/usuarios/{id}` | Exclui permanentemente um usuário. | Autenticado |
| GET | `/api/v1/usuarios/{id}/jogos` | Retorna a biblioteca de jogos adquiridos pelo usuário. | Autenticado |

### 4.3. Catálogo e Vitrine (/api/v1/jogos)

| Método | Rota | Descrição | Permissão |
|--------|------|-----------|-----------|
| GET | `/api/v1/jogos` | Retorna todos os jogos disponíveis na vitrine da loja. | Pública |
| GET | `/api/v1/jogos/{id}` | Retorna detalhes técnicos de um jogo específico. | Pública |
| GET | `/api/v1/jogos/buscar?nome=` | Pesquisa jogos por substring no nome. | Pública |
| POST | `/api/v1/jogos` | Cadastra um novo jogo no catálogo. | Admin |
| PUT | `/api/v1/jogos/{id}` | Edita nome, preço, tipo ou imagem de um jogo. | Admin |
| DELETE | `/api/v1/jogos/{id}` | Deleta um jogo e suas referências em biblioteca/compras. | Admin |

### 4.4. Compras (/api/v1/compras)

| Método | Rota | Descrição | Permissão |
|--------|------|-----------|-----------|
| POST | `/api/v1/compras` | Realiza a compra de um jogo para o usuário autenticado. | Autenticado |
| GET | `/api/v1/compras` | Lista todo o histórico geral de transações do sistema. | Admin |
| GET | `/api/v1/compras/{id}` | Retorna o recibo de uma compra pelo seu identificador. | Admin |

---

## 5. Como Executar a Aplicação

### 5.1. Execução via Docker Compose (Ambiente Isolado com PostgreSQL)

O projeto possui um arquivo `docker-compose.yml` na raiz que orquestra o banco de dados PostgreSQL e o backend Spring Boot automaticamente:

```bash
# Compilar o artefato Jar e iniciar os contêineres em background
./mvnw clean package -DskipTests
docker compose up --build -d
```

- A API estará disponível em: `http://localhost:8080`
- O banco PostgreSQL é executado na porta interna `5432` (mapeada para a porta externa `5434`).
- Ao subir pela primeira vez, o banco é migrado pelo Hibernate e pré-populado com usuários administrativos, clientes e catálogo de jogos via `data.sql`.

### 5.2. Execução Local com Maven Wrapper

Para rodar localmente na máquina de desenvolvimento:

```bash
./mvnw spring-boot:run
```

---

## 6. Testes Automatizados

O projeto conta com testes unitários e de integração implementados com JUnit 5, Mockito e Spring Boot Test, executando em banco em memória H2 no perfil de teste sem interferir no banco de dados PostgreSQL.

Para rodar todos os testes automatizados da bateria:

```bash
./mvnw clean test
```

A bateria cobre:
- Validação de regras em `CompraServiceTest` (saldo insuficiente, compra de jogo duplicado, sucesso em transação).
- Verificação de JWT em `JwtServiceTest` (geração de assinatura, parse, expiração).
- Controladores em `AuthControllerTest` e `CompraControllerTest`.
- Serviços de Usuário e integração externa em `UserServiceTest` e `IgdbIntegrationServiceTest`.
