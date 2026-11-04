# Connectivity Forecast API

Reimplementação Java/Spring Boot da [API de referência](https://github.com/LiniiS/connectivity-forecast-api), usada como artefato educacional de APS II.

## Executar

**Requisitos:** Java 17 e Maven 3.9+. Confira as versões instaladas:

```bash
java -version   # deve indicar 17 ou superior
mvn -version    # deve indicar 3.9 ou superior
```

**Passo a passo** (na pasta raiz do projeto, onde está o `pom.xml`):

```bash
mvn test                # roda os testes automatizados
mvn spring-boot:run     # sobe a API em http://localhost:8080
```

Com a API no ar, teste em outro terminal:

```bash
curl http://localhost:8080/api/v1/health
curl http://localhost:8080/api/v1/models
```

Para encerrar, use `Ctrl+C` no terminal onde a API está rodando. Se a porta 8080 estiver ocupada, altere `server.port` em `src/main/resources/application.properties`.

**Documentação interativa:**

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI (JSON): `http://localhost:8080/v3/api-docs`

**Principais recursos** (prefixo `/api/v1`): `health`, `models`, `locations`, `forecasts` e `activity/check`.

## Limitações

Este projeto é um artefato educacional. **Não o interprete como um serviço pronto para uso real:**

- As previsões vêm de dados **fictícios e estáticos** (`data/predictions.csv` e `data/models.json`); nenhum modelo é treinado ou executado.
- A API **não consulta o RIPE Atlas** nem qualquer fonte externa de medições.
- Classificação de qualidade e recomendações são **regras experimentais**, sem validação científica.
- Não há banco de dados, autenticação nem deploy.

## Escopo

API local com catálogo de modelos e previsões mockadas. Fixture igual à referência: 4 modelos ativos, 10 probes e 24 instantes (960 previsões); catálogo também contém modelo inativo. Dados fictícios. Não consulta RIPE Atlas, não treina nem executa modelos, não usa banco de dados e não exige deploy. Classificação e recomendações são regras experimentais, não padrões científicos.

## Estrutura

Separação em controllers, services, repositories, modelos de domínio/DTOs e configuração. API versionada em `/api/v1`, com recursos de health, modelos, localizações, previsões e atividades.

## Documentação

README e OpenAPI são pontos de partida. Completar em exercício: descrição dos endpoints, parâmetros e validações, exemplos de requisição/resposta, códigos de erro e origem dos campos. Referência à licença MIT da API de origem preservada neste projeto.
