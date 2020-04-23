package wooteco.chess.database.dao;

import wooteco.chess.database.DBConnector;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.utils.BoardConverter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChessDao {
    private final Connection conn;

    public ChessDao() {
        this.conn = DBConnector.getConnection();
    }

    public Board load(int roomId) throws SQLException {
        String query = "SELECT board, is_white FROM chess WHERE room_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, roomId);
        ResultSet rs = pstmt.executeQuery();
        if (!rs.next()) {
            return null;
        }
        String boardInformation = rs.getString("board");
        Team turn = rs.getBoolean("is_white") ? Team.WHITE : Team.BLACK;
        return BoardConverter.convertToBoard(boardInformation, turn);
    }

    public void save(int roomId, Board board) throws SQLException {
        Team turn = board.getTurn();
        String boardInformation = BoardConverter.convertToString(board);
        String query = "INSERT INTO chess (room_id, board, is_white) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE board = ?, is_white = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, roomId);
        pstmt.setString(2, boardInformation);
        pstmt.setBoolean(3, turn.isWhite());
        pstmt.setString(4, boardInformation);
        pstmt.setBoolean(5, turn.isWhite());
        pstmt.executeUpdate();
    }

    public void delete(int roomId) throws SQLException {
        String query = "DELETE FROM chess WHERE room_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, roomId);
        pstmt.executeUpdate();
    }

    public int createRoom(Board board) throws SQLException {
        String boardInformation = BoardConverter.convertToString(board);
        boolean isWhite = (board.getTurn() == Team.WHITE);

        String query = "INSERT INTO chess (board, is_white) VALUES (?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, boardInformation);
        pstmt.setBoolean(2, isWhite);
        pstmt.executeUpdate();

        query = "SELECT LAST_INSERT_ID()";
        pstmt = conn.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        if (!rs.next()) {
            return 0;
        }
        return rs.getInt(1);
    }

    public List<Integer> loadRoomNumbers() throws SQLException {
        String query = "SELECT room_id FROM chess";
        PreparedStatement pstmt = conn.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        if (!rs.next()) {
            return null;
        }

        List<Integer> roomNumbers = new ArrayList<>();
        do {
            roomNumbers.add(rs.getInt("room_id"));
        } while (rs.next());
        return roomNumbers;
    }
}
