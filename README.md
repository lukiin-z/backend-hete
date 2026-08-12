# 📌 Sobre o Projeto

Este projeto foi desenvolvido como parte da disciplina **Advanced Programming e Mobile Development**.
O objetivo é criar um **backend para gerenciamento de consultas médicas**, permitindo o cadastro e manutenção de **pacientes** e **especialidades**, além de operações básicas de **CRUD**.

---

# 🚀 Tecnologias Utilizadas

* Java 17
* Spring Boot *(estrutura inicial gerada pelo Spring Initializr)*
* Maven *(gerenciamento de dependências)*
* Spring Web *(para criação da API REST)*
* Spring Data JPA *(para persistência de dados)*
* Banco de dados configurável *(H2, MySQL ou outro, conforme ajustes futuros)*

---

# 📂 Estrutura do Projeto

```
src/main/java
 ├── entities        → Paciente, Especialidade
 ├── controllers     → Endpoints REST para manipulação dos dados
 ├── services        → Regras de negócio
 └── repositories    → Interfaces JPA para persistência

src/main/resources   → Configurações (application.properties)

data/                → Scripts ou dados auxiliares

pom.xml              → Configuração do Maven
```

---

# ⚙️ Funcionalidades Implementadas

## Pacientes

* Criar novo paciente (**POST**)
* Listar pacientes (**GET**)
* Atualizar paciente (**PUT**)
* Deletar paciente (**DELETE**)

## Especialidades

* Criar nova especialidade (**POST**)
* Listar especialidades (**GET**)
* Atualizar especialidade (**PUT**)
* Deletar especialidade (**DELETE**)

---

# 📡 Endpoints Principais

| Método | Endpoint             | Descrição               |
| ------ | -------------------- | ----------------------- |
| POST   | /pacientes           | Cadastrar paciente      |
| GET    | /pacientes           | Listar pacientes        |
| PUT    | /pacientes/{id}      | Atualizar paciente      |
| DELETE | /pacientes/{id}      | Remover paciente        |
| POST   | /especialidades      | Cadastrar especialidade |
| GET    | /especialidades      | Listar especialidades   |
| PUT    | /especialidades/{id} | Atualizar especialidade |
| DELETE | /especialidades/{id} | Remover especialidade   |

---

# ▶️ Como Executar

### 1️⃣ Clone o repositório

```bash
git clone https://github.com/An4lu/backend-consultas.git
```

### 2️⃣ Acesse a pasta do projeto

```bash
cd backend-consultas
```

### 3️⃣ Compile e rode a aplicação

```bash
./mvnw spring-boot:run
```

### 4️⃣ A API estará disponível em

```
http://localhost:8080
```

---
