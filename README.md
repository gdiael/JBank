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

A cada execução bem-sucedida do pipeline, uma tag no formato `build-NNN` é gerada automaticamente, identificando de forma única cada configuração válida.
