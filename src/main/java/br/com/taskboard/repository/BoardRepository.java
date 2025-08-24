package br.com.taskboard.repository;


import br.com.taskboard.model.Board;
import br.com.taskboard.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BoardRepository {

    public int salvar(Board board) {
        String sql = "INSERT INTO board (nome) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, board.getNome());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    board.setId(id);
                    return id;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }


    public List<Board> listar() {
        List<Board> boards = new ArrayList<>();
        String sql = "SELECT id, nome FROM board ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Board b = new Board();
                b.setId(rs.getInt("id"));
                b.setNome(rs.getString("nome"));
                boards.add(b);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return boards;
    }


    public void excluir(int id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
// apagar colunas e cards por FK (se não houver ON DELETE CASCADE)
            try (PreparedStatement p1 = conn.prepareStatement("DELETE FROM card WHERE column_id IN (SELECT id FROM column_board WHERE board_id = ?)");
                 PreparedStatement p2 = conn.prepareStatement("DELETE FROM column_board WHERE board_id = ?");
                 PreparedStatement p3 = conn.prepareStatement("DELETE FROM board WHERE id = ?")) {
                p1.setInt(1, id); p1.executeUpdate();
                p2.setInt(1, id); p2.executeUpdate();
                p3.setInt(1, id); p3.executeUpdate();
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
