package chess.dao;

import chess.domain.game.room.Room;
import chess.domain.game.room.RoomId;
import chess.domain.game.room.RoomPassword;
import chess.domain.piece.PieceColor;
import chess.exception.IncorrectPassword;
import chess.exception.NotFoundRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoomDaoImpl implements RoomDao {
    private static final String TABLE_NAME = "room";
    private static final String WHITE_TURN = "WHITE";
    private static final String BLACK_TURN = "BLACK";

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Room> roomRowMapper =
            (resultSet, rowNum) -> {
                String id = resultSet.getString("id");
                String title = resultSet.getString("title");
                String password = resultSet.getString("password");
                return Room.from(id, title, password);
            };

    @Autowired
    public RoomDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createRoom(Room room) {
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
    public void deleteRoom(RoomId roomId, RoomPassword roomPassword) {
        String query = String.format("DELETE FROM %s WHERE id = ? AND password = ?", TABLE_NAME);
        int update = jdbcTemplate.update(query, roomId.getValue(), roomPassword.getValue());

        if (update == 0) {
            throw new IncorrectPassword();
        }
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
        try {
            String turn = jdbcTemplate.queryForObject(query, (resultSet, rowNum) -> resultSet.getString("turn"),
                    roomId.getValue());
            return PieceColor.valueOf(turn);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundRoom();
        }
    }
}
