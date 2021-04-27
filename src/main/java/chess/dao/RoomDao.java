package chess.dao;


import chess.domain.room.Players;
import chess.domain.room.Room;
import chess.domain.room.RoomInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
public class RoomDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Long create(Room room) {
        String sql = "insert into room (name, pw, white_player, game_id) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"game_id"});
            preparedStatement.setString(1, room.getName());
            preparedStatement.setString(2, room.getPw());
            preparedStatement.setString(3, room.getWhitePlayer());
            preparedStatement.setLong(4, room.getGameId());
            return preparedStatement;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Room load(Long roomId) {
        String sql = "select * from room where room_id = ?";
        return jdbcTemplate.queryForObject(sql, roomRowMapper, roomId);
    }

    public List<Room> loadAll() {
        String sql = "select * from room";
        return jdbcTemplate.query(sql, roomRowMapper);
    }

    public void setBlackPlayer(final String blackPlayer, final Long roomId) {
        String sql = "update room set black_player = ? where room_id = ?";
        jdbcTemplate.update(sql, blackPlayer, roomId);
    }

    private final RowMapper<Room> roomRowMapper = (resultSet, rowNum) -> {
        Long id = resultSet.getLong("room_id");
        String name = resultSet.getString("name");
        String pw = resultSet.getString("pw");
        Long gameId = resultSet.getLong("game_id");
        String whitePlayer = resultSet.getString("white_player");
        String blackPlayer = resultSet.getString("black_player");
        return new Room(id, new RoomInfo(name, pw, gameId), new Players(whitePlayer, blackPlayer));
    };
}
