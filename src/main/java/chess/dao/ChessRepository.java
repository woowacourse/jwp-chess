package chess.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.position.Position;

@Repository
public class ChessRepository {

    private final JdbcTemplate jdbcTemplate;

    public ChessRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<Position, Piece> findPiecesByRoomId(int roomId) {
        String sql = "select * from pieces where room_id=?";

        Map<Position, Piece> boardInfo = new HashMap<>();
        jdbcTemplate.query(
            sql,
            (resultSet, rowNum) -> boardInfo.put(
                new Position(resultSet.getString("position")),
                PieceFactory.of(resultSet.getString("piece_name"))
            ), roomId);
        return boardInfo;
    }

    public List<String> findAllRoomName() {
        String sql = "select title from room";

        return jdbcTemplate.query(sql,
            (resultSet, rowNum) -> resultSet.getString("title"));
    }

    public void insertRoom(String title) {
        String sql = "insert into room (title, turn, playing_flag) values (?, 'WHITE', true)";
        jdbcTemplate.update(sql, title);
    }

    public String findTurnByRoomId(int roomId) {
        String sql = "select turn from room where id=?";
        return jdbcTemplate.queryForObject(sql, String.class, roomId);
    }

    public boolean findPlayingFlagByRoomId(int roomId) {
        String sql = "select playing_flag from room where id=?";
        return jdbcTemplate.queryForObject(sql, Boolean.class, roomId);
    }

    public void updateRoom(int roomId, boolean isBlackTurn, boolean playing) {
        String sql = "update room set turn = ?, playing_flag = ? where id=?";

        if (isBlackTurn) {
            jdbcTemplate.update(sql, Color.BLACK.name(), playing, roomId);
            return;
        }

        jdbcTemplate.update(sql, Color.WHITE.name(), playing, roomId);
    }

    public void updatePiecesByRoomId(int roomId, Map<Position, Piece> pieces) {
        deleteAllPiecesByRoomId(roomId);
        for (Map.Entry<Position, Piece> piece : pieces.entrySet()) {
            insertPieceByRoomId(roomId, piece.getValue().getName(), piece.getKey().chessCoordinate());
        }
    }

    public void deleteAllPiecesByRoomId(int roomId) {
        String sql = "delete from pieces where room_id=?";
        jdbcTemplate.update(sql, roomId);
    }

    public void insertPieceByRoomId(int roomId, String pieceName, String position) {
        String sql = "insert into pieces (room_id, piece_name, position) values(?,?,?)";
        jdbcTemplate
            .update(sql, roomId, pieceName, position);
    }

    public void deleteRoomById(int roomId) {
        String sql = "delete from room where id=?";
        jdbcTemplate.update(sql, roomId);
    }

    public String findIdByTitle(String roomTitle) {
        String sql = "select id from room where title=?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, roomTitle);
        } catch (RuntimeException e) {
            return null;
        }
    }
}
