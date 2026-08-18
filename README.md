# Backend de consultas médicas

API REST para cadastro de especialidades, médicos, pacientes e consultas. O projeto foi desenvolvido em Java 17 com Spring Boot, Spring Web MVC, Spring Data JPA e H2.

## Funcionalidades

- CRUD de especialidades, médicos, pacientes e consultas;
- filtros de consultas por médico e por paciente;
- DTOs de entrada e saída, sem expor diretamente as entidades JPA;
- validação de dados, datas futuras e regras de negócio;
- bloqueio de choque de horário para médico ou paciente;
- fluxo de status controlado (`agendada → confirmada → realizada` ou cancelamento);
- respostas de erro padronizadas para validação, conflitos e recursos inexistentes;
- persistência local em H2;
- massa de demonstração opcional no perfil `dev`;
- CORS configurável por variável de ambiente.

## Requisitos

- JDK 17;
- nenhuma instalação global do Maven é necessária: o projeto inclui Maven Wrapper.

## Executando

```bash
git clone https://github.com/lukiin-z/backend-hete.git
cd backend-hete
./mvnw spring-boot:run
```

No Windows, use `mvnw.cmd spring-boot:run`. A API inicia em `http://localhost:8080` e o console H2 fica em `http://localhost:8080/h2-console`.

Para iniciar com médicos, pacientes e consultas de demonstração:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Configuração padrão do H2:

- JDBC URL: `jdbc:h2:file:./data/consultas`
- usuário: `sa`
- senha: vazia

O banco é criado em `data/` e não é versionado.

## Endpoints

| Método | Rota | Descrição |
| --- | --- | --- |
| `GET` | `/especialidades` | Lista especialidades |
| `GET` | `/especialidades/{id}` | Busca uma especialidade |
| `POST` | `/especialidades` | Cria uma especialidade |
| `PUT` | `/especialidades/{id}` | Atualiza uma especialidade |
| `DELETE` | `/especialidades/{id}` | Exclui uma especialidade |
| `GET` | `/medicos` | Lista médicos |
| `GET` | `/medicos/{id}` | Busca um médico |
| `POST` | `/medicos` | Cria um médico |
| `PUT` | `/medicos/{id}` | Atualiza um médico |
| `DELETE` | `/medicos/{id}` | Exclui um médico |
| `GET` | `/pacientes` | Lista pacientes |
| `GET` | `/pacientes/{id}` | Busca um paciente |
| `POST` | `/pacientes` | Cria um paciente |
| `PUT` | `/pacientes/{id}` | Atualiza um paciente |
| `DELETE` | `/pacientes/{id}` | Exclui um paciente |
| `GET` | `/consultas` | Lista consultas |
| `GET` | `/consultas/{id}` | Busca uma consulta |
| `GET` | `/consultas/medico/{id}` | Filtra por médico |
| `GET` | `/consultas/paciente/{id}` | Filtra por paciente |
| `POST` | `/consultas` | Agenda uma consulta |
| `PUT` | `/consultas/{id}` | Atualiza parcialmente os dados da consulta |
| `PATCH` | `/consultas/{id}/status` | Executa uma transição de status |
| `DELETE` | `/consultas/{id}` | Exclui uma consulta |

Os status aceitos são `agendada`, `confirmada`, `cancelada` e `realizada`.

### Exemplo de consulta

Médico e paciente devem existir antes do agendamento:

```json
{
  "medicoId": 1,
  "pacienteId": 1,
  "dataHora": "2026-08-20T10:00:00",
  "valor": 250.00,
  "observacoes": "Consulta de rotina"
}
```

Uma nova consulta sempre nasce como `agendada`. Para confirmar:

```http
PATCH /consultas/1/status
Content-Type: application/json

{ "status": "confirmada" }
```

Transições permitidas:

| Status atual | Próximos status |
| --- | --- |
| `agendada` | `confirmada` ou `cancelada` |
| `confirmada` | `realizada` ou `cancelada` |
| `realizada` | estado final |
| `cancelada` | estado final |

## Configuração

Para liberar outras origens do frontend, informe uma lista separada por vírgulas:

```bash
CORS_ALLOWED_ORIGINS=http://localhost:8081,http://192.168.0.10:8081
```

## Testes

```bash
./mvnw test
```

## Estrutura

```text
src/
├── main/
│   ├── java/com/fiap/ec/backend_consultas/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── exception/
│   │   ├── model/
│   │   ├── repository/
│   │   └── service/
│   └── resources/application.properties
└── test/java/com/fiap/ec/backend_consultas/
```
