# Catálogo SQL: demonstração da Situação-Problema 1

Projeto Java mínimo que mostra, na prática:

1. uma busca **vulnerável** a SQL Injection (SQL montado por concatenação);
2. dois ataques contra ela (condição sempre verdadeira e `UNION` vazando outra tabela);
3. a **correção** com `PreparedStatement`;
4. uma **modelagem melhorada** da tabela (tipos, restrições e índices).

Usa o banco **H2 em memória**: não precisa instalar MySQL nem PostgreSQL, e tudo some quando o programa termina.

## O que você precisa

- JDK 25 ou superior (`java -version` e `javac -version` devem funcionar)
- Maven (`mvn -v`), **ou** uma IDE (Eclipse, IntelliJ, NetBeans)

## Como rodar com Maven

Dentro da pasta `catalogo-sql`:

```
mvn compile exec:java
```

Na primeira vez o Maven baixa o H2 e o plugin de execução (precisa de internet).

## Como rodar na IDE (sem Maven)

1. Crie um projeto Java e copie a pasta `src/main/java/catalogo` para dentro dele.
2. Baixe o arquivo `h2-2.2.224.jar` no site oficial do H2 (h2database.com) ou no Maven Central.
3. Adicione o `.jar` às bibliotecas do projeto.
4. Execute a classe `catalogo.Principal`.

## O que deve aparecer (esperado)

Eu compilei o código, mas **não consegui executá-lo no meu ambiente** (sem acesso para baixar o H2). Estes são os resultados esperados pela lógica do código; confira na sua máquina:

| Etapa | Esperado |
|-------|----------|
| 1. Busca vulnerável, termo `Dom` | 1 resultado (Dom Casmurro) |
| 2. Ataque `' OR 1=1 --` | os 3 itens do catálogo |
| 2. Ataque `UNION` | 2 linhas com `admin` e `maria` e suas senhas fictícias |
| 3. Busca segura, termo `Dom` | 1 resultado |
| 3. Mesmos ataques na busca segura | 0 resultados |
| 4. Inserções válidas | 2 itens inseridos |
| 4. Ano 3000, tipo `Jogo`, título nulo | 3 inserções RECUSADAS pelo banco |

Se algo for diferente, anote e me mande a saída completa.

## Mapa do código

- `Banco.java`: conexão e criação das tabelas (a tabela inicial do aluno e a melhorada).
- `ItemDAO.java`: `buscarVulneravel` (errada), `buscarSeguro` (certa), `inserir` e `buscarItemMidia`.
- `Principal.java`: roda as etapas e imprime o resultado de cada uma.

## Para o relatório

- Tire **capturas de tela** da saída de cada etapa (antes e depois da correção).
- **Anote o tempo** que você gastou de verdade em cada parte.
- Veja o arquivo `rascunho_relatorio.md`.

## Observação sobre `LIKE`

O `PreparedStatement` impede a injeção, mas os caracteres `%` e `_` digitados pelo usuário continuam funcionando como coringas do `LIKE`. Isso não é SQL Injection, só uma busca mais ampla do que o usuário pretendia.
