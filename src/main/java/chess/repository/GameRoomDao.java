package chess.repository;

import chess.repository.entity.GameRoomEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class GameRoomDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public GameRoomDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void save(final GameRoomEntity gameRoomEntity) {
        String insertSql = "insert into game_room (game_room_id, name, password) values (:gameRoomId, :name, :password)";
        SqlParameterSource source = new BeanPropertySqlParameterSource(gameRoomEntity);
        namedParameterJdbcTemplate.update(insertSql, source);
    }

    public void delete(final String gameRoomId, final String password) {
        String deleteSql = "delete from game_room where game_room_id=:gameRoomId and password=:password";
        Map<String, String> values = new HashMap<>();
        values.put("gameRoomId", gameRoomId);
        values.put("password", password);
        SqlParameterSource source = new MapSqlParameterSource(values);
        namedParameterJdbcTemplate.update(deleteSql, source);
    }

    public GameRoomEntity load(final String gameRoomId) {
        String selectSql = "select game_room_id, name, password from game_room where game_room_id=:gameRoomId";
        SqlParameterSource source = new MapSqlParameterSource("gameRoomId", gameRoomId);
        return namedParameterJdbcTemplate.queryForObject(selectSql, source, getGameRoomEntityRowMapper());
    }

    private RowMapper<GameRoomEntity> getGameRoomEntityRowMapper() {
        return (rs, rn) -> new GameRoomEntity(
                rs.getString("game_room_id"),
                rs.getString("name"),
                rs.getString("password")
        );
    }

    public List<GameRoomEntity> loadAll() {
        String selectSql = "select * from game_room";
        return namedParameterJdbcTemplate.query(selectSql, getGameRoomEntityRowMapper());
    }
}
