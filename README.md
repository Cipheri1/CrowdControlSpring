# CrowdControl – versão web (Spring REST)

Etapa 9 do PI. API REST em Spring Boot que reaproveita o núcleo da etapa 6
(model, repository, service) e grava os dados no MySQL por JDBC.

## Como executar
1. Rode `sql/crowdcontrol_bd.sql` no MySQL (cria o banco `crowdcontrol_db`).
2. Crie o arquivo `config/application.properties` na raiz do projeto com:
   `crowdcontrol.db.password=SUA_SENHA` (e, se precisar, `crowdcontrol.db.user=...`).
3. No NetBeans, **Run Project**. A aplicação sobe em http://localhost:8080.

## Estrutura
- `br.com.crowdcontrol.model | repository | service | util | exception | config`: núcleo da etapa 6 (sem Spring).
- `br.com.crowdcontrol.web`: camada web (configuração dos beans, controllers REST, DTOs).
- `src/test`: testes JUnit da etapa 7.
