package chess.dao;

import chess.domain.game.room.Room;
import chess.domain.game.room.RoomId;
import chess.domain.piece.PieceColor;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class GameRoomDaoImpl implements GameRoomDao {
    private static final String TABLE_NAME = "game_room";
    private static final String WHITE_TURN = "WHITE";
    private static final String BLACK_TURN = "BLACK";

    private final JdbcTemplate jdbcTemplate;

    private RowMapper<Room> roomRowMapper =
            (resultSet, rowNum) -> {
                String id = resultSet.getString("id");
                String title = resultSet.getString("title");
                String password = resultSet.getString("password");
                return Room.from(id, title, password);
            };

    @Autowired
    public GameRoomDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createGameRoom(Room room) {
        String query = String.format("INSERT INTO %s VALUES (?, ?, ?, 'WHITE')", TABLE_NAME);
        jdbcTemplate.update(query, room.getId().getValue(), room.getRoomTitle().getValue(),
                room.getPassword().getValue());
    }

    @Override
    public List<Room> getRooms() {
        String query = String.format("SELECT id, title, password FROM %s", TABLE_NAME);
        return jdbcTemplate.query(query, roomRowMapper);
    }

    @Override
    public void deleteGameRoom(RoomId roomId) {
        String query = String.format("DELETE FROM %s WHERE id = ?", TABLE_NAME);
        jdbcTemplate.update(query, roomId.getValue());
    }

    @Override
    public void updateTurnToWhite(RoomId roomId) {
        updateTurn(roomId, WHITE_TURN);
    }

    @Override
    public void updateTurnToBlack(RoomId roomId) {
        updateTurn(roomId, BLACK_TURN);
    }

    private void updateTurn(RoomId roomId, String turn) {
        String query = String.format("UPDATE %s SET turn = ? WHERE id = ?", TABLE_NAME);
        jdbcTemplate.update(query, turn, roomId.getValue());
    }

    @Override
    public PieceColor getCurrentTurn(RoomId roomId) {
        String query = String.format("SELECT turn FROM %s WHERE id = ?", TABLE_NAME);
        String turn = jdbcTemplate.queryForObject(query, (resultSet, rowNum) -> resultSet.getString("turn"),
                roomId.getValue());
        return PieceColor.valueOf(turn);
    }
}
