package chess.repository;

import chess.domain.chessgame.ChessGame;
import chess.domain.chessgame.Room;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.position.Position;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RoomRepositoryImpl implements RoomRepository {

    private final JdbcTemplate jdbcTemplate;

    public RoomRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Integer> findAllPlayingRoomId() {
        String sql = "select id from room where playing_flag=true";

        return jdbcTemplate.query(sql, (resultSet, rowNum) -> resultSet.getInt("id"));
    }

    public Room findRoomByRoomId(int roomId) {
        return new Room(roomId, new ChessGame(findPiecesByRoomId(roomId),
            findPlayingFlagByRoomId(roomId), findTurnByRoomId(roomId)));
    }

    public void insertRoom(Room room) {
        String sql = "insert into room (id, turn, playing_flag) values (?, 'WHITE', true)";

        jdbcTemplate.update(sql, room.getId());
    }

    public void insertPieces(Room room) {
        String sql = "insert into pieces (room_id, piece_name, position) values(?,?,?)";

        for (Map.Entry<Position, Piece> entry : room.pieces().entrySet()) {
            jdbcTemplate.update(sql, room.getId(), entry.getValue().getName(),
                entry.getKey().chessCoordinate());
        }
    }

    public boolean findPlayingFlagByRoomId(int roomId) {
        String sql = "select playing_flag from room where id=?";

        return jdbcTemplate.queryForObject(sql, Boolean.class, roomId);
    }

    public Map<Position, Piece> findPiecesByRoomId(int roomId) {
        String sql = "select * from pieces where room_id=?";
        Map<Position, Piece> pieces = new HashMap<>();

        jdbcTemplate.query(sql, (resultSet, rowNum) -> pieces
            .put(new Position(resultSet.getString("position")),
                PieceFactory.of(resultSet.getString("piece_name"))), roomId);

        return pieces;
    }

    public void updateChessGameByRoom(Room room) {
        String sql = "update room set turn = ?, playing_flag = ? where id=?";

        if (room.isBlackTurn()) {
            jdbcTemplate.update(sql, Color.BLACK.name(), room.isPlaying(), room.getId());
            return;
        }

        jdbcTemplate.update(sql, Color.WHITE.name(), room.isPlaying(), room.getId());
    }

    public void updatePiecesByRoom(Room room) {
        deleteAllPiecesByRoom(room);
        insertPieces(room);
    }

    public void deleteAllPiecesByRoom(Room room) {
        String sql = "delete from pieces where room_id=?";

        jdbcTemplate.update(sql, room.getId());
    }

    public Color findTurnByRoomId(int roomId) {
        String sql = "select turn from room where id=?";

        return Color.valueOf(jdbcTemplate.queryForObject(sql, String.class, roomId));
    }

}
