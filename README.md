# Jbank

Projeto para a disciplina ```DIM0517 - Gerência de Configuração e Mudanças```.

## Alunos

- Yuri Maximiliano Brasileiro Santos (user: BrasileiroYuri)
- Gdiael Souto Barros (user: gdiael)

## Stack

Nesse projeto, usaremos Java com Spring Boot Framework.

## Instruções para execução

### Requisitos

- Java 17
- Maven

### Passo a passo

Execute o seguinte comando no terminal.

```
cd jbank
./mvnw spring-boot:run
```

Obs.: caso esteja usando windows troque `./mvnw` por `nvnw.cmd`

## Pipeline CI/CD

O projeto utiliza **GitHub Actions** para integração e entrega contínuas.

### Gatilhos

O pipeline é executado a cada `push` ou `pull request` na branch `develop`.

### Etapas

| Etapa | Descrição |
|---|---|
| Resolver dependências | `./mvnw dependency:resolve` |
| Build | `./mvnw compile` |
| Testes unitários | `./mvnw test` |
| Imagem Docker | `docker build -t jbank:<build>` |
| Tag | Cria tag `build-<número>` no repositório |

### Tags

A cada execução bem sucedida do pipeline, uma tag no formato `build-NNN` é gerada automaticamente, identificando de forma única cada configuração válida.

### Rodando com Docker

A imagem Docker está disponível no Docker Hub: **<https://hub.docker.com/r/gdiael/jbank>**

Para baixar e executar a versão mais recente:

```bash
docker pull gdiael/jbank:latest
docker run -p 8080:8080 gdiael/jbank:latest
```

Para usar uma build específica, substitua `latest` pela tag desejada (ex: `build-007`):

```bash
docker run -p 8080:8080 gdiael/jbank:build-007
```

A aplicação ficará disponível em `http://localhost:8080`.

## Endpoints

Base URL: `http://localhost:8080/banco/conta`

---

### Criar conta

`POST /banco/conta/`

O campo `type` aceita os valores `SIMPLES`, `BONUS` ou `POUPANCA`. Para contas do tipo `BONUS`, o campo `balance` é ignorado.

```bash
# Conta simples
curl -X POST http://localhost:8080/banco/conta/ \
  -H "Content-Type: application/json" \
  -d '{"type": "SIMPLES", "number": 1001, "balance": 500.00}'

# Conta bônus
curl -X POST http://localhost:8080/banco/conta/ \
  -H "Content-Type: application/json" \
  -d '{"type": "BONUS", "number": 1002}'

# Conta poupança
curl -X POST http://localhost:8080/banco/conta/ \
  -H "Content-Type: application/json" \
  -d '{"type": "POUPANCA", "number": 1003, "balance": 1000.00}'
```

**Resposta (201 Created):**

```json
{ "tipo": "SIMPLES", "numero": 1001, "saldo": 500.0 }
```

> Retorna `409 Conflict` se o número de conta já existir.

---

### Consultar conta

`GET /banco/conta/{id}`

```bash
curl http://localhost:8080/banco/conta/1001
```

**Resposta (200 OK):**

```json
{ "tipo": "SIMPLES", "numero": 1001, "saldo": 500.0 }
```

> Para contas do tipo `BONUS`, a resposta inclui o campo `"bonus"` com a pontuação atual.

---

### Consultar saldo

`GET /banco/conta/{id}/saldo`

```bash
curl http://localhost:8080/banco/conta/1001/saldo
```

**Resposta (200 OK):**

```json
{ "numero": 1001, "saldo": 500.0 }
```

---

### Crédito (depósito)

`PUT /banco/conta/{id}/credito`

```bash
curl -X PUT http://localhost:8080/banco/conta/1001/credito \
  -H "Content-Type: application/json" \
  -d '{"amount": 250.00}'
```

**Resposta (200 OK):** retorna os dados atualizados da conta.

> Retorna `422 Unprocessable Entity` se o valor for negativo ou a conta não existir.

---

### Débito (saque)

`PUT /banco/conta/{id}/debito`

```bash
curl -X PUT http://localhost:8080/banco/conta/1001/debito \
  -H "Content-Type: application/json" \
  -d '{"amount": 100.00}'
```

**Resposta (200 OK):** retorna os dados atualizados da conta.

> Retorna `422 Unprocessable Entity` se o valor for negativo, saldo insuficiente ou a conta não existir.

---

### Transferência

`PUT /banco/conta/transferencia`

```bash
curl -X PUT http://localhost:8080/banco/conta/transferencia \
  -H "Content-Type: application/json" \
  -d '{"from": 1001, "to": 1002, "amount": 150.00}'
```

**Resposta:** `200 OK` em caso de sucesso, `422 Unprocessable Entity` caso contrário.

---

### Render juros (poupança)

`PUT /banco/conta/rendimento`

Aplica a taxa de juros informada a todas as contas poupança. O campo `rate` é uma fração decimal (`0.01` equivale a 1%).

```bash
curl -X PUT http://localhost:8080/banco/conta/rendimento \
  -H "Content-Type: application/json" \
  -d '{"rate": 0.01}'
```

**Resposta:** `200 OK`.

---
