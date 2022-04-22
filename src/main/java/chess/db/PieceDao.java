package chess.db;

import chess.domain.piece.Color;
import chess.domain.piece.InitialPositionPieceGenerator;
import chess.domain.piece.Piece;
import chess.domain.position.Column;
import chess.domain.position.Row;
import chess.domain.position.Square;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PieceDao {
    private static final String ERROR_MESSAGE_NO_SAVED_GAME = "헉.. 저장 안한거 아냐? 그런 게임은 없어!";
    private final static String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/chess";
    private static final String USER = "user";
    private static final String PASSWORD = "password";
    private org.springframework.jdbc.datasource.DriverManagerDataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public PieceDao() {
        dataSource = new org.springframework.jdbc.datasource.DriverManagerDataSource(URL, USER, PASSWORD);
        dataSource.setDriverClassName(DRIVER);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void save(String gameID) {
        String sql = "insert into piece (position, type, color, gameID) values (?, ?, ?, ?)";

        List<Object[]> data = new ArrayList<>();
        insertPieces(gameID, data);
        jdbcTemplate.batchUpdate(sql, data);
    }

    private void insertPieces(String gameID, List<Object[]> data) {
        for (Column column : Column.values()) {
            insertPiecesInRow(gameID, data, column);
        }
    }

    private void insertPiecesInRow(String gameID, List<Object[]> data, Column column) {
        for (Row row : Row.values()) {
            data.add(new Object[]{new Square(column, row).getName(),
                    InitialPositionPieceGenerator.getType(column, row).name(),
                    InitialPositionPieceGenerator.getColor(row).name(),
                    gameID});
        }
    }

    public void deleteByPosition(Square target, String gameID) {
        String sql = "delete from piece where position = ? and gameID = ?";
        jdbcTemplate.update(sql, target.getName(), gameID);
    }

    public void updatePosition(Square source, Square target, String gameID) {
        String sql = "update piece set position = ? where position = ? and gameID = ?";
        jdbcTemplate.update(sql, target.getName(), source.getName(), gameID);
    }

    public void insertNone(String gameID, Square source) {
        String sql = "insert into piece (position, type, color, gameID) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, source.getName(), InitialPositionPieceGenerator.NONE.name(), Color.NONE.name(), gameID);
    }

    public void deleteAll(String gameID) {
        String sql = "delete from piece where gameID = ?";
        jdbcTemplate.update(sql, gameID);
    }

    public Map<Square, Piece> findByGameID(String gameID) {
        String sql = "select position, type, color from piece where gameID = ?";
        Map<Square, Piece> board = new HashMap<>();
        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql, gameID);
        loadBoard(board, data);
        checkGameExist(board);
        return board;
    }

    private void loadBoard(Map<Square, Piece> board, List<Map<String, Object>> pieces) {
        for (Map<String, Object> piece : pieces) {
            String position = (String) piece.get("position");
            String type = (String) piece.get("type");
            String color = (String) piece.get("color");
            board.put(new Square(position), Piece.createByTypeAndColor(type, color));
        }
    }

    private void checkGameExist(Map<Square, Piece> board) {
        if (board.isEmpty()) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NO_SAVED_GAME);
        }
    }
}
