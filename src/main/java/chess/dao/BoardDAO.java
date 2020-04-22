package chess.dao;

import chess.dto.BoardDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class BoardDAO {
    private static BoardDAO instance = new BoardDAO();

    private BoardDAO() {
    }

    public static BoardDAO getInstance() {
        return instance;
    }

    public void saveBoard(BoardDTO boardDTO) throws SQLException {
        Map<String, String> board = boardDTO.getBoard();

        String query = "INSERT INTO board VALUES (?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            for (String position : board.keySet()) {
                pstmt.setString(1, position);
                pstmt.setString(2, board.get(position));
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    public void deletePreviousBoard() throws SQLException {
        String query = "DELETE FROM board";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.executeUpdate();
        }
    }

    public BoardDTO getBoard() throws SQLException {
        Map<String, String> board = new HashMap<>();
        String query = "SELECT * FROM board";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                board.put(rs.getString(1), rs.getString(2));
            }
            return new BoardDTO(board);
        }
    }
}

