package wooteco.chess.dao;

import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.dto.BoardDTO;
import wooteco.chess.dto.GameStatusDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ch.qos.logback.core.db.DBHelper.closeConnection;

public class BoardDAO {
    private static final String SERVER = "localhost:13306";
    private static final String DATABASE = "db_name";
    private static final String OPTION = "?useSSL=false&serverTimezone=UTC";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "root";
    private static final String DESTINATION = String.format("jdbc:mysql://%s/%s%s", SERVER, DATABASE, OPTION);

    private static BoardDAO boardDAO = new BoardDAO();

    private BoardDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(" !! JDBC Driver load 오류: " + e.getMessage());
        }
    }

    public static BoardDAO getInstance() {
        return boardDAO;
    }

    public void placePieceOn(BoardDTO boardDTO) throws SQLException {
        String query = "INSERT INTO board (position, piece) VALUES (?, ?) ON DUPLICATE KEY UPDATE position=?, piece=?";

        executeUpdate(query, ps -> {
            ps.setString(1, boardDTO.getPosition());
            ps.setString(2, boardDTO.getPiece());
            ps.setString(3, boardDTO.getPosition());
            ps.setString(4, boardDTO.getPiece());
        });
    }

    //read
    public Optional<Piece> findPieceOn(Position position) throws SQLException {
        String query = "SELECT * FROM board WHERE position = ?";

        return executeQuery(query, ps -> {
            ps.setString(1, position.toString());
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            }

            return Optional.of(Piece.of(rs.getString("piece")));
        });
    }

    public List<BoardDTO> findAllPieces() throws SQLException {
        String query = "SELECT * FROM board ";

        return executeQuery(query, ps -> {
            ResultSet rs = ps.executeQuery();

            List<BoardDTO> boardDTOs = new ArrayList<>();
            while (rs.next()) {
                BoardDTO boardDTO = new BoardDTO();

                boardDTO.setPiece(rs.getString("piece"));
                boardDTO.setPosition(rs.getString("position"));
                boardDTOs.add(boardDTO);
            }
            return boardDTOs;
        });
    }

    public void initializeTurn() throws SQLException {
        String query = "TRUNCATE TABLE game_status";

        executeUpdate(query, (ps) -> {
        });
    }

    public void removePieceOn(Position position) throws SQLException {
        String query = "DELETE FROM board WHERE position = ?";

        executeUpdate(query, (ps) -> {
            ps.setString(1, position.toString());
        });
    }

    public void updateTurn(GameStatusDTO gameStatusDTO) throws SQLException {
        String query = "INSERT INTO game_status (current_turn) VALUES (?) ON DUPLICATE KEY UPDATE current_turn=?";

        executeUpdate(query, (ps) -> {
            ps.setString(1, gameStatusDTO.getCurrentTeam());
            ps.setString(2, gameStatusDTO.getCurrentTeam());
        });
    }

    private void executeUpdate(String query, throwingConsumer<PreparedStatement> consumer) throws SQLException {
        try (Connection con = DriverManager.getConnection(DESTINATION, USER_NAME, PASSWORD);
             PreparedStatement ps = con.prepareStatement(query)) {
            consumer.accept(ps);
            ps.executeUpdate();

            ps.close();
            closeConnection(con);
        }
    }

    private <R> R executeQuery(String query, throwingFunction<PreparedStatement, R> function) throws SQLException {
        try (Connection con = DriverManager.getConnection(DESTINATION, USER_NAME, PASSWORD);
             PreparedStatement ps = con.prepareStatement(query)) {
            R r = function.accept(ps);

            ps.close();
            closeConnection(con);
            return r;
        }
    }

    public GameStatusDTO readCurrentTurn() throws SQLException {
        String query = "SELECT * FROM game_status";

        return executeQuery(query, ps -> {
            ResultSet rs = ps.executeQuery();
            rs.next();
            GameStatusDTO gameStatusDTO = new GameStatusDTO();
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
