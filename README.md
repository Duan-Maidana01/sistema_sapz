# SAGSA / SAPZ — API RESTful (Spring Boot 3.5 + JWT)

Back-end do sistema SAGSA/SAPZ (Situações de Aprendizagem — Metodologia SENAI), construído no mesmo padrão
arquitetural do projeto de referência `api_aptec` (pacotes, DTOs, `fromDTO`, Services com validação de permissão,
exceções customizadas, `GlobalExceptionHandler`, Spring Security + JWT).

## Como executar

1. Crie o banco executando o script `CREATE_DATABASE_sistema_sapz.sql` (MySQL 8+ ou MariaDB 10.3+).
2. Ajuste `src/main/resources/application.properties`:
   - **MySQL** → `spring.datasource.url=jdbc:mysql://localhost:3306/sistema_sapz?serverTimezone=America/Sao_Paulo`
   - **MariaDB** → `spring.datasource.url=jdbc:mariadb://localhost:3306/sistema_sapz`
     (o driver MySQL não funciona com MariaDB 11+; os dois drivers já estão no `pom.xml`)
   - usuário/senha do banco e `jwt.secret` (mínimo 64 caracteres).
3. `./mvnw spring-boot:run` (Java 21).
4. Na primeira execução são criados os perfis **ADMIN, COORDENADOR, INSTRUTOR** e o administrador
   `admin@sagsa.com.br` / `Admin@123` (configurável em `app.admin.*`; troque a senha após o login).

`spring.jpa.hibernate.ddl-auto=none`: o Hibernate não altera o banco. Para conferir o mapeamento use `validate`
(testado com MariaDB 11.4 + o script oficial: todas as 29 tabelas validadas).

## Autenticação

```http
POST /login
{ "email": "admin@sagsa.com.br", "senha": "Admin@123" }
```
Resposta: cabeçalho `Authorization: Bearer <token>` + corpo `{ token, tipo, expiraEmMs, id, nome, email, perfil }`.
Envie `Authorization: Bearer <token>` em todas as demais requisições. Usuários com `ativo = false` não fazem login
e o token deles deixa de valer imediatamente.

## Estrutura de pacotes (`com.sagsa.api`)

```text
configs/        SecurityConfig, WebConfig, DataInitializer (perfis + admin inicial)
controllers/    1 controller REST por recurso
exceptions/     GlobalExceptionHandler, ErrorResponse, AuthorizationException
models/         Entidades JPA (26 entidades + 3 tabelas N:N via @ManyToMany/@JoinTable; 2 chaves compostas @EmbeddedId)
  dtos/         *RequestDTO / *CreateDTO / *UpdateDTO (entrada)  e  *ResponseDTO (saída, com fromEntity)
  enums/        PerfilEnum, TipoCursoEnum, TipoModuloEnum, TipoCapacidadeEnum, DominioCapacidadeEnum,
                TipoEstrategiaEnum, TipoMetodoAvaliacaoEnum, TipoCompartilhamentoEnum, StatusMatriculaEnum
  projections/  UsuarioProjection, SituacaoAprendizagemProjection
repositories/   JpaRepository de cada entidade
security/       JWTUtil, JWTAuthenticationFilter, JWTAuthorizationFilter, UserSpringSecurity
services/       Regras de negócio + permissões  (exceptions/: ObjectNotFound, DataBindingViolation, BusinessRule)
```

## Perfis e permissões

| Recurso | Leitura | Escrita |
|---|---|---|
| Perfis, Escolas, Eixos tecnológicos | autenticado | ADMIN |
| Usuários | ADMIN/COORDENADOR (todos), demais só `/usuarios/me` | ADMIN cria/exclui; cada um edita os próprios dados (perfil/status só ADMIN) |
| Áreas, Cursos, Planos, Módulos, UCs, Funções, Subfunções, Padrões, Capacidades, Conhecimentos, Alunos | autenticado | ADMIN ou COORDENADOR |
| Turmas, Docentes x UC, Matrículas | ADMIN/COORD: todas; INSTRUTOR: as suas | ADMIN ou o COORDENADOR responsável pela turma |
| Situações de Aprendizagem e itens (estratégias, recursos, instrumentos, critérios, rubricas) | autor, ADMIN, COORDENADOR ou quem recebeu compartilhamento | autor ou ADMIN |
| Compartilhamentos | quem vê a SA | autor ou ADMIN |
| Histórico de PDF | quem vê a SA | registro: autor/ADMIN/COORD; exclusão: ADMIN |

**Visibilidade por compartilhamento:** `GLOBAL` (todos), `USUARIO` (destinatário), `ESCOLA` / `CURSO`
(usuários que lecionam ou coordenam turmas daquela escola / daquele curso).

## Endpoints

Padrão CRUD: `GET /recurso`, `GET /recurso/{id}`, `POST /recurso` (201 + `Location`), `PUT /recurso/{id}` (204),
`DELETE /recurso/{id}` (204).

**1. Autenticação / Usuários / Perfis**
- `POST /login`
- `/usuarios` (CRUD) · `GET /usuarios/me` · `GET /usuarios/perfil/{INSTRUTOR|COORDENADOR|ADMIN}`
- `/perfis` (CRUD)

**2. Estrutura organizacional e curricular**
- `/escolas` · `/eixos-tecnologicos` · `/areas-tecnologicas` (+ `/eixo/{id}`) · `/cursos` (+ `/area/{id}`)
- `/planos-curso` (+ `/curso/{id}`) · `/modulos` (+ `/plano/{id}`) · `/unidades-curriculares` (+ `/modulo/{id}`)
- `/funcoes` (+ `/plano/{id}`) · `/subfuncoes` (+ `/funcao/{id}`) · `/padroes-desempenho` (+ `/subfuncao/{id}`)
- `/capacidades` (+ `/uc/{id}?tipo=TECNICA`) · `/conhecimentos` (+ `/uc/{id}`)

**3. Turmas / Alunos / Docentes**
- `/turmas` (+ `/minhas`, `/escola/{id}`, `/plano/{id}`) · `/alunos`
- `/docentes-ucs`: `POST`, `GET /turma/{id}`, `GET /instrutor/{id}`, `GET /minhas`,
  `DELETE /turma/{t}/uc/{u}/instrutor/{i}`
- `/matriculas`: `POST`, `GET /turma/{id}`, `GET /aluno/{id}`, `GET|PUT|DELETE /turma/{t}/aluno/{a}`

**4. SAPZ / Avaliação**
- `/situacoes-aprendizagem` (CRUD) + `/minhas`, `/compartilhadas-comigo`, `/uc/{id}`, `/{id}/detalhe`
  (visão completa para o PDF), `POST /{id}/duplicar` (cópia completa para o usuário logado)
- `/estrategias-ensino`, `/recursos-ambientes`, `/instrumentos-avaliacao` (+ `/situacao/{saId}`)
- `/criterios-avaliacao` (+ `/instrumento/{id}`) · `/criterio-rubricas` (+ `/criterio/{id}`)
- `/compartilhamentos`: `POST`, `GET /{id}`, `GET /situacao/{saId}`, `DELETE /{id}`
- `/historico-pdf`: `POST`, `GET /{id}`, `GET /situacao/{saId}`, `DELETE /{id}`

## Principais regras de negócio

- Turma: `dataFim` > `dataInicio`; código único; coordenador precisa ter perfil COORDENADOR/ADMIN e estar ativo;
  plano de curso ativo. Coordenador que cria turma sem responsável assume a coordenação.
- Docente x UC: a UC deve pertencer ao plano de curso da turma; docente com perfil INSTRUTOR/COORDENADOR e ativo.
- Matrícula: turma ativa, sem duplicidade; padrões `dataMatricula = hoje`, `status = MATRICULADO`.
- Capacidade: padrões de desempenho devem ser do mesmo plano de curso da UC.
- SA: autor = usuário logado; carga horária ≤ carga da UC; capacidades/conhecimentos da mesma UC; turma do mesmo
  plano; INSTRUTOR só vincula turma/UC em que leciona; não remove capacidade já usada em critério.
- Critério: a capacidade avaliada precisa estar na SA. Rubrica: nível único por critério; instrumento DICOTOMICO
  aceita apenas níveis 1 e 2.
- Compartilhamento: replica a CHECK `chk_compartilhamento_alvo` com mensagens claras; sem alvos duplicados;
  mantém `situacoes_aprendizagem.compartilhada` sincronizado.
- Histórico de PDF: versão automática (1.0, 2.0…) quando omitida; hash SHA-256 opcional; registro imutável.

## Formato de erro

```json
{ "status": 422, "message": "Erro de validação...", "errors": [ { "field": "nome", "message": "não deve estar em branco" } ] }
```
401 não autenticado · 403 sem permissão · 404 não encontrado · 409 duplicidade/vínculo · 422 validação/regra de negócio.

## Enums

Os valores dos enums são gravados como texto. Se o banco já tiver dados com outros valores (ex.: em `tipo_curso`),
ajuste as constantes em `models/enums/`.

## Teste ponta a ponta

`scripts/teste_e2e.py` exercita login, catálogo, turmas, SA, avaliação, compartilhamento e permissões
(68 verificações). Rode com a API na porta 8090 e o banco recém-criado.
