package br.com.taskboard.repository;


import br.com.taskboard.model.Board;
import br.com.taskboard.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BoardRepository {

    public void salvar(Board board) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO board (nome) VALUES (?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, board.getNome());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                board.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Board> listar() {
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

    public void excluir(int id) {
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
