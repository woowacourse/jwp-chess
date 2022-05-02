package chess.database.dao.spring;

import chess.database.dto.RoomDto;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDao {

    private final JdbcTemplate jdbcTemplate;

    public RoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public RoomDto create(RoomDto roomDto) {
        String sql = "insert into room(name, password) values (?, ?)";
        jdbcTemplate.update(sql, roomDto.getName(), roomDto.getPassword());
        return findByName(roomDto.getName());
    }

    public RoomDto findByName(String roomName) {
        String sql = "select * from room where name = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql,
                (resultSet, rowNum) -> {
                    int id = resultSet.getInt("id");
                    String password = resultSet.getString("password");
                    return new RoomDto(id, roomName, password);
                }, roomName))
            .orElseThrow(() -> new IllegalArgumentException("[ERROR] 해당하는 이름의 게임방이 없습니다."));
    }

    public RoomDto findById(int roomId) {
        String sql = "select * from room where id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, (resultSet, rowNum) -> {
                String roomName = resultSet.getString("name");
                String password = resultSet.getString("password");
                return new RoomDto(roomId, roomName, password);
            }, roomId))
            .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 방입니다."));
    }

    public void delete(int roomId) {
        final String sql = "DELETE FROM room WHERE id = ?";
        jdbcTemplate.update(sql, roomId);
    }

    public List<RoomDto> findAll() {
        String sql = "select id, name from room";
        return jdbcTemplate.query(sql, (resultSet, rowNum) ->
            new RoomDto(resultSet.getInt("id"), resultSet.getString("name"))
        );
    }

    public boolean existRoomName(String roomName) {
        String sql = "select count(*) from room where name = ?";
        Optional<Integer> count = Optional.ofNullable(jdbcTemplate.queryForObject(sql, Integer.class, roomName));
        return count.isPresent() && count.get() >= 1;
    }

    public boolean existRoomId(int roomId) {
        String sql = "select count(*) from room where id = ?";
        Optional<Integer> count = Optional.ofNullable(jdbcTemplate.queryForObject(sql, Integer.class, roomId));
        return count.isPresent() && count.get() >= 1;
    }
}
