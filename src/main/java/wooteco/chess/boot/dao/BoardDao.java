package wooteco.chess.boot.dao;

import org.springframework.stereotype.Component;
import wooteco.chess.boot.dto.BoardDto;
import wooteco.chess.boot.dto.GameStatusDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static ch.qos.logback.core.db.DBHelper.closeConnection;

@Component
public class BoardDao {

    private static final String SERVER = "localhost:13306";
    private static final String DATABASE = "db_name";
    private static final String OPTION = "?useSSL=false&serverTimezone=UTC";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "root";
    private static final String DESTINATION = String.format("jdbc:mysql://%s/%s%s", SERVER, DATABASE, OPTION);

    private BoardDao() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(" !! JDBC Driver load 오류: " + e.getMessage());
        }
    }

    public void placePieceOn(final BoardDto boardDTO) throws SQLException {
        String query = "INSERT INTO board (position, piece) VALUES (?, ?) ON DUPLICATE KEY UPDATE position=?, piece=?";

        executeUpdate(query, ps -> {
            ps.setString(1, boardDTO.getPosition());
            ps.setString(2, boardDTO.getPiece());
            ps.setString(3, boardDTO.getPosition());
            ps.setString(4, boardDTO.getPiece());
        });
    }

    public List<BoardDto> findAllPieces() throws SQLException {
        String query = "SELECT * FROM board ";

        return executeQuery(query, ps -> {
            List<BoardDto> boardDTOs = new ArrayList<>();

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BoardDto boardDTO = new BoardDto();

                boardDTO.setPiece(rs.getString("piece"));
                boardDTO.setPosition(rs.getString("position"));
                boardDTOs.add(boardDTO);
            }
            return boardDTOs;
        });
    }

    public void updateTurn(final GameStatusDto gameStatusDTO) throws SQLException {
        initializeTurn();
        String query = "INSERT INTO game_status (current_turn) VALUES (?) ON DUPLICATE KEY UPDATE current_turn=?";

        executeUpdate(query, (ps) -> {
            ps.setString(1, gameStatusDTO.getCurrentTeam());
            ps.setString(2, gameStatusDTO.getCurrentTeam());
        });
    }

    private void initializeTurn() throws SQLException {
        String query = "TRUNCATE TABLE game_status";

        executeUpdate(query, (ps) -> {
        });
    }

    private void executeUpdate(final String query, final throwingConsumer<PreparedStatement> consumer) throws SQLException {
        try (Connection con = DriverManager.getConnection(DESTINATION, USER_NAME, PASSWORD);
             PreparedStatement ps = con.prepareStatement(query)) {
            consumer.accept(ps);
            ps.executeUpdate();

            ps.close();
            closeConnection(con);
        }
    }

    private <R> R executeQuery(final String query, final throwingFunction<PreparedStatement, R> function) throws SQLException {
        try (Connection con = DriverManager.getConnection(DESTINATION, USER_NAME, PASSWORD);
             PreparedStatement ps = con.prepareStatement(query)) {
            R result = function.accept(ps);

            ps.close();
            closeConnection(con);
            return result;
        }
    }

    public GameStatusDto readCurrentTurn() throws SQLException {
        String query = "SELECT * FROM game_status";

        return executeQuery(query, ps -> {
            ResultSet rs = ps.executeQuery();
            rs.next();
            GameStatusDto gameStatusDTO = new GameStatusDto();
            gameStatusDTO.setCurrentTeam(rs.getString("current_turn"));
            return gameStatusDTO;
        });
    }

    private interface throwingConsumer<T> {
        void accept(T t) throws SQLException;
    }

    private interface throwingFunction<T, R> {
        R accept(T t) throws SQLException;
    }
}
