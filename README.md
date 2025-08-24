# 🧭 TaskBoard – Console + MySQL

![Java](https://img.shields.io/badge/Java-17-blue)
![Maven](https://img.shields.io/badge/Maven-Build-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8.x-orange)
![JUnit](https://img.shields.io/badge/JUnit-5-red)

Gerenciador de boards/colunas/cards via **terminal**, com **persistência em MySQL**.  
Este README é **interativo**: contém _checklists_, _blocos copiáveis_, _dicas_ e explicações **linha a linha** dos principais arquivos.

---

## 📋 Sumário
- [Requisitos do Desafio](#-requisitos-do-desafio)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Modelagem & SQL](#-modelagem--sql)
- [Configuração do Ambiente](#-configuração-do-ambiente)
- [Como Executar](#-como-executar)
- [Testes Automatizados](#-testes-automatizados)
- [Explicação do Código (linha a linha)](#-explicação-do-código-linha-a-linha)
  - [`DatabaseConnection.java`](#databaseconnectionjava)
  - [`Main.java`](#mainjava)
  - [`BoardController.java` (menu principal)](#boardcontrollerjava-menu-principal)
  - [`BoardRepository.java`](#boardrepositoryjava)
  - [`ColumnRepository.java`](#columnrepositoryjava)
- [Resolução de Problemas](#-resolução-de-problemas)
- [Atalhos úteis](#-atalhos-úteis)

---

## ✅ Requisitos do Desafio

- [x] **Menu principal** com: Criar novo board, Selecionar board, Excluir boards, Sair.  
- [x] **Persistência de boards** no MySQL.  
- [x] **Persistência de colunas** (repositório incluído).  
- [ ] **Persistência de cards** (estrutura de modelo pronta; repositório pode ser adicionado).  
- [ ] **Menu de manipulação do board selecionado** (mover/bloquear/desbloquear/cancelar card).  
- [ ] **Relatórios opcionais** (tempo por coluna; bloqueios).  

> ⚠️ Observação: Este repositório/documento foi preparado para o código disponibilizado.  
> Caso você já tenha implementado `CardRepository` e o menu do board, basta complementar as seções correspondentes.

---

## 🧱 Estrutura do Projeto

```
desafio-projeto-board/
├─ pom.xml
├─ src/
│  ├─ main/
│  │  ├─ java/br/com/taskboard/
│  │  │  ├─ Main.java
│  │  │  ├─ controller/BoardController.java
│  │  │  ├─ model/
│  │  │  │  ├─ Board.java
│  │  │  │  ├─ Column.java
│  │  │  │  ├─ ColumnType.java
│  │  │  │  └─ Card.java
│  │  │  ├─ repository/
│  │  │  │  ├─ BoardRepository.java
│  │  │  │  └─ ColumnRepository.java
│  │  │  └─ util/DatabaseConnection.java
│  └─ test/
│     └─ java/br/com/taskboard/
│        └─ MainTest.java
```

---

## 🗃️ Modelagem & SQL

**Entidades principais**  
- **board**: `id`, `nome`  
- **column_board**: `id`, `name`, `type`, `board_id`  
- **card** (_sugerida_): `id`, `titulo`, `descricao`, `data_criacao`, `bloqueado`, `motivo_bloqueio`, `coluna_id`, `data_entrada_coluna`, `data_saida_coluna`

> **Importante**: No seu código atual, `Board` usa campo **`nome`** (não `name`).

### Script SQL (copiar/colar)

```sql
-- Banco
CREATE DATABASE IF NOT EXISTS taskboard;
USE taskboard;

-- Tabela de boards
CREATE TABLE IF NOT EXISTS board (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

-- Tabela de colunas
CREATE TABLE IF NOT EXISTS column_board (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    board_id INT NOT NULL,
    CONSTRAINT fk_column_board_board
      FOREIGN KEY (board_id) REFERENCES board(id)
      ON DELETE CASCADE
);

-- Tabela de cards (sugerida)
CREATE TABLE IF NOT EXISTS card (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(120) NOT NULL,
    descricao TEXT,
    data_criacao DATETIME NOT NULL,
    bloqueado BOOLEAN NOT NULL DEFAULT 0,
    motivo_bloqueio VARCHAR(255),
    coluna_id INT,
    data_entrada_coluna DATETIME,
    data_saida_coluna DATETIME,
    CONSTRAINT fk_card_column
      FOREIGN KEY (coluna_id) REFERENCES column_board(id)
      ON DELETE SET NULL
);
```

---

## 🛠️ Configuração do Ambiente

### 1) Variáveis de Ambiente (recomendado)

**Windows (PowerShell)**
```powershell
setx DB_URL "jdbc:mysql://localhost:3306/taskboard?useSSL=false&serverTimezone=UTC"
setx DB_USER "root"
setx DB_PASSWORD "SUA_SENHA_AQUI"
```

**Linux/macOS (bash/zsh)**
```bash
export DB_URL="jdbc:mysql://localhost:3306/taskboard?useSSL=false&serverTimezone=UTC"
export DB_USER="root"
export DB_PASSWORD="SUA_SENHA_AQUI"
```

> Feche e reabra o terminal (ou reinicie o IntelliJ) após definir as variáveis.

### 2) Dependências Maven (trecho do `pom.xml`)

```xml
<dependencies>
    <!-- MySQL Connector/J (coordenadas novas) -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <version>8.0.33</version>
    </dependency>

    <!-- Testes (JUnit 5) -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>

<build>
    <plugins>
        <!-- Compilador -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.13.0</version>
            <configuration>
                <source>17</source>
                <target>17</target>
            </configuration>
        </plugin>

        <!-- Execução via Maven -->
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>exec-maven-plugin</artifactId>
            <version>3.5.1</version>
            <configuration>
                <mainClass>br.com.taskboard.Main</mainClass>
            </configuration>
        </plugin>

        <!-- Testes JUnit 5 -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>3.2.5</version>
            <configuration>
                <useSystemClassLoader>true</useSystemClassLoader>
            </configuration>
        </plugin>
    </plugins>
</build>
```

---

## ▶️ Como Executar

Na pasta do projeto (`C:\Users\SEU_USER\Downloads\desafio-projeto-board`):

```bash
mvn clean compile
mvn exec:java
```

**Atalho PowerShell (opcional):** crie `start-taskboard.ps1` com:

```powershell
$projectPath = "C:\Users\SEU_USER\Downloads\desafio-projeto-board"
Set-Location $projectPath
mvn exec:java
```

Rode uma vez (se necessário, habilite scripts):
```powershell
Set-ExecutionPolicy RemoteSigned -Scope CurrentUser
```

---

## ✅ Testes Automatizados

### 1) Classe de Teste (copia e cole em `src/test/java/br/com/taskboard/MainTest.java`)

> O teste injeta `System.in` com a opção **4 (Sair)** para o programa iniciar, exibir o menu e finalizar sem bloquear o teste.  
> Também captura `System.out` para validar o texto do menu.

```java
package br.com.taskboard;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainTest {

    private final PrintStream originalOut = System.out;
    private final java.io.InputStream originalIn = System.in;
    private ByteArrayOutputStream saida;

    @BeforeEach
    void setup() {
        // simula entrada "4\n" (Sair) para não travar o teste
        String input = "4\n";
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        // captura saída do console
        saida = new ByteArrayOutputStream();
        System.setOut(new PrintStream(saida));
    }

    @AfterEach
    void teardown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    void deveExibirMenuPrincipalAoIniciar() {
        Main.main(new String[]{});
        String console = saida.toString(StandardCharsets.UTF_8);

        assertTrue(console.contains("1 - Criar novo board"));
        assertTrue(console.contains("2 - Selecionar board"));
        assertTrue(console.contains("3 - Excluir boards"));
        assertTrue(console.contains("4 - Sair"));
    }
}
```

### 2) Rodando os testes

```bash
mvn test
```

Saída esperada:
```
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

---

## 🔍 Explicação do Código (linha a linha)

> Abaixo, os **arquivos-chave** explicados com comentários embutidos para você entender cada instrução.

### `DatabaseConnection.java`

```java
package br.com.taskboard.util;               // pacote utilitário para classes de infra

import java.sql.Connection;                   // interface de conexão JDBC
import java.sql.DriverManager;                // fábrica de conexões JDBC
import java.sql.SQLException;                 // exceção checked de SQL

public class DatabaseConnection {             // classe utilitária de conexão
    public static Connection getConnection() throws SQLException {   // método estático para obter conexão
        String url = System.getenv("DB_URL");                        // lê URL do banco das variáveis de ambiente
        String user = System.getenv("DB_USER");                      // lê usuário
        String password = System.getenv("DB_PASSWORD");              // lê senha

        if (url == null || user == null || password == null) {       // valida se tudo foi configurado
            throw new RuntimeException("Variáveis de ambiente do banco não configuradas!");
        }

        return DriverManager.getConnection(url, user, password);     // abre e retorna a conexão JDBC
    }
}
```

---

### `Main.java`

```java
package br.com.taskboard;                    // pacote raiz da aplicação

import br.com.taskboard.controller.BoardController; // importa o controlador do menu

public class Main {                           // classe inicial
    public static void main(String[] args) {  // ponto de entrada da aplicação
        BoardController.menuPrincipal();      // delega para o menu principal (loop do console)
    }
}
```

---

### `BoardController.java` (menu principal)

```java
package br.com.taskboard.controller;

import br.com.taskboard.model.Board;                  // modelo Board
import br.com.taskboard.repository.BoardRepository;   // repositório de persistência
import java.util.List;                                // utilitários de coleção
import java.util.Scanner;                             // leitura do console

public class BoardController {
    private static final Scanner scanner = new Scanner(System.in);  // leitor único para stdin
    private static final BoardRepository boardRepo = new BoardRepository(); // repositório compartilhado

    public static void menuPrincipal() {              // exibe e controla o menu
        while (true) {                                // loop até usuário escolher sair
            System.out.println("1 - Criar novo board");   // opção 1
            System.out.println("2 - Selecionar board");   // opção 2
            System.out.println("3 - Excluir boards");     // opção 3
            System.out.println("4 - Sair");               // opção 4
            System.out.print("Escolha: ");                // prompt
            int opcao = scanner.nextInt();                // lê opção
            scanner.nextLine();                           // consome quebra de linha

            switch (opcao) {                              // decide ação
                case 1: criarBoard(); break;              // cria board
                case 2: selecionarBoard(); break;         // seleciona/abre board
                case 3: excluirBoards(); break;           // exclui board
                case 4: System.exit(0);                   // encerra aplicação
                default: System.out.println("Opção inválida!");
            }
        }
    }

    private static void criarBoard() {                    // fluxo de criação
        System.out.print("Nome do board: ");              // pergunta o nome
        String nome = scanner.nextLine();                 // lê nome
        Board board = new Board();                        // instancia modelo
        board.setNome(nome);                              // atribui nome
        boardRepo.salvar(board);                          // persiste no MySQL
        System.out.println("Board criado com sucesso!");  // feedback
    }

    private static void selecionarBoard() {               // fluxo de seleção
        List<Board> boards = boardRepo.listar();          // carrega todos os boards
        if (boards.isEmpty()) {                           // se não houver
            System.out.println("Nenhum board disponível!");
            return;
        }
        boards.forEach(b -> System.out.println(b.getId() + " - " + b.getNome())); // lista IDs
        System.out.print("Escolha o ID do board: ");      // pede ID
        int id = scanner.nextInt();                       // lê ID
        scanner.nextLine();                               // consome quebra
        // TODO: Chamar menu de manipulação do board selecionado (mover/bloquear/cancelar cards)
    }

    private static void excluirBoards() {                 // fluxo de exclusão
        List<Board> boards = boardRepo.listar();          // lista boards
        boards.forEach(b -> System.out.println(b.getId() + " - " + b.getNome())); // imprime
        System.out.print("Escolha o ID do board para excluir: ");
        int id = scanner.nextInt();                       // lê ID
        scanner.nextLine();                               // consome quebra
        boardRepo.excluir(id);                            // exclui no banco (cascade apaga colunas/cards se houver)
        System.out.println("Board excluído com sucesso!");
    }
}
```

---

### `BoardRepository.java`

```java
package br.com.taskboard.repository;

import br.com.taskboard.model.Board;                 // modelo Board
import br.com.taskboard.util.DatabaseConnection;     // util de conexão
import java.sql.*;                                   // JDBC
import java.util.ArrayList;
import java.util.List;

public class BoardRepository {

    public void salvar(Board board) {                // insere novo board
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO board (nome) VALUES (?)";      // usa coluna 'nome'
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, board.getNome());                        // bind do parâmetro
            ps.executeUpdate();                                      // executa insert

            ResultSet rs = ps.getGeneratedKeys();                    // recupera ID gerado
            if (rs.next()) {
                board.setId(rs.getInt(1));
            }
        } catch (SQLException e) {                                   // log simples
            e.printStackTrace();
        }
    }

    public List<Board> listar() {                    // retorna todos os boards
        List<Board> boards = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM board";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Board board = new Board();
                board.setId(rs.getInt("id"));
                board.setNome(rs.getString("nome"));
                boards.add(board);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return boards;
    }

    public void excluir(int id) {                     // apaga board por ID
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM board WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

---

### `ColumnRepository.java`

```java
package br.com.taskboard.repository;

import br.com.taskboard.model.Column;               // modelo Column
import br.com.taskboard.model.ColumnType;           // enum ColumnType
import br.com.taskboard.util.DatabaseConnection;    // conexão JDBC
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ColumnRepository {

    // INSERT
    public int salvar(Column c) {
        String sql = "INSERT INTO column_board (name, type, board_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, c.getName());
            stmt.setString(2, c.getType().name());
            stmt.setInt(3, c.getBoardId());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1); // ID gerado
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // SELECT por ID
    public Column buscarPorId(int id) {
        String sql = "SELECT * FROM column_board WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Column(
                        rs.getInt("id"),
                        rs.getString("name"),
                        ColumnType.valueOf(rs.getString("type")),
                        rs.getInt("board_id")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // SELECT por board
    public List<Column> buscarPorBoard(int boardId) {
        List<Column> cols = new ArrayList<>();
        String sql = "SELECT * FROM column_board WHERE board_id = ? ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, boardId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cols.add(new Column(
                        rs.getInt("id"),
                        rs.getString("name"),
                        ColumnType.valueOf(rs.getString("type")),
                        rs.getInt("board_id")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cols;
    }

    // UPDATE
    public boolean atualizar(Column c) {
        String sql = "UPDATE column_board SET name = ?, type = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getName());
            stmt.setString(2, c.getType().name());
            stmt.setInt(3, c.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // DELETE
    public boolean remover(int id) {
        String sql = "DELETE FROM column_board WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
```

---

## 🆘 Resolução de Problemas

- **`Unknown lifecycle phase ".mainClass=..."`**  
  → Use `mvn exec:java -Dexec.mainClass="br.com.taskboard.Main"` ou configure o `exec-maven-plugin` e rode só `mvn exec:java`.

- **`Variáveis de ambiente do banco não configuradas!`**  
  → Defina `DB_URL`, `DB_USER`, `DB_PASSWORD` (veja seção de configuração) e reabra o terminal/IDE.

- **`Access denied for user 'root'@'localhost'`**  
  → Verifique usuário/senha, permissões no MySQL e se o serviço está ativo.

- **`java.sql.SQLSyntaxErrorException: Table ... doesn't exist`**  
  → Rode o SQL da seção **Modelagem & SQL**.

---

## ⌨️ Atalhos úteis

Comandos frequentes:
```bash
mvn clean compile
mvn exec:java
mvn test
mvn -q exec:java
```

PowerShell para abrir o projeto e rodar:
```powershell
cd C:\Users\SEU_USER\Downloads\desafio-projeto-board
mvn exec:java
```

---

> Dúvidas ou melhorias? Abra uma issue (ou deixe um comentário) e vamos evoluir esse board! 🚀
