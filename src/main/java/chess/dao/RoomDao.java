package chess.dao;

import chess.dto.RoomDto;
import chess.entity.RoomEntity;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<RoomEntity> roomEntityRowMapper = (resultSet, rowNum) -> RoomEntity.of(
            resultSet.getInt("game_id"),
            resultSet.getString("room_name"),
            resultSet.getString("room_password"),
            resultSet.getString("status")
    );

    public RoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(RoomEntity roomEntity) {
        String sql = "insert into room (game_id, room_name, room_password, status) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                roomEntity.getGameId(),
                roomEntity.getRoomName(),
                roomEntity.getRoomPassword(),
                roomEntity.getStatus());
    }

    public List<RoomEntity> findAll() {
        String sql = "select * from room";
        return jdbcTemplate.query(sql, roomEntityRowMapper);
    }

    public void delete(RoomEntity roomEntity) {
        String sql = "delete from room where game_id=?";
        jdbcTemplate.update(sql, roomEntity.getGameId());
    }

    public RoomEntity findById(RoomEntity roomEntity) {
        String sql = "select * from room where game_id=?";
        return jdbcTemplate.queryForObject(sql, roomEntityRowMapper, roomEntity.getGameId());
    }

    public void updateById(RoomEntity roomEntity) {
        String sql = "update room set room_name=?, room_password=?, status=? where game_id=?";
        jdbcTemplate.update(sql,
                roomEntity.getRoomName(),
                roomEntity.getRoomPassword(),
                roomEntity.getStatus(),
                roomEntity.getGameId());
    }
}
