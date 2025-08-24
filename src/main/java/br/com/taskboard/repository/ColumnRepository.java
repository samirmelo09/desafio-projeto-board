package br.com.taskboard.repository;

import br.com.taskboard.model.Column;
import br.com.taskboard.model.ColumnType;
import br.com.taskboard.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ColumnRepository {

    // Salvar nova coluna no banco
    public int salvar(Column c) {
        String sql = "INSERT INTO column_board (name, type, board_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, c.getName());
            stmt.setString(2, c.getType().name());
            stmt.setInt(3, c.getBoardId());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // Retorna o ID da coluna inserida
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Buscar coluna por ID
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

    // Buscar todas as colunas de um board
    public List<Column> buscarPorBoard(int boardId) {
        List<Column> colunas = new ArrayList<>();
        String sql = "SELECT * FROM column_board WHERE board_id = ? ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, boardId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Column coluna = new Column(
                            rs.getInt("id"),
                            rs.getString("name"),
                            ColumnType.valueOf(rs.getString("type")),
                            rs.getInt("board_id")
                    );
                    colunas.add(coluna);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return colunas;
    }

    // Atualizar coluna
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

    // Remover coluna
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
