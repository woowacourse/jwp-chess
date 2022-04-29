package chess.repository;

import chess.web.dto.RoomDto;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDao {

    private static final String TABLE_NAME = "room";
    private static final String KEY_NAME = "id";
    private static final int KEY_INDEX = 1;
    private static final int NAME_INDEX = 2;

    private final SimpleJdbcInsert insertActor;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public RoomDao(DataSource dataSource,
                   NamedParameterJdbcTemplate jdbcTemplate) {
        this.insertActor = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_NAME);
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(String name, String password) {
        return insertActor.executeAndReturnKey(Map.of("name", name, "password", password)).intValue();
    }

    public List<RoomDto> findAll() {
        String sql = "select * from room";
        return jdbcTemplate.query(sql,
                (resultSet, rowNum) ->
                        new RoomDto(resultSet.getInt(KEY_INDEX), resultSet.getString(NAME_INDEX),
                                resultSet.getString(2)));
    }

    public Optional<RoomDto> find(String name) {
        String sql = "select * from room where name = :name";
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(sql, Map.of("name", name),
                            (resultSet, rowNum) ->
                                    new RoomDto(resultSet.getInt(KEY_INDEX), resultSet.getString(NAME_INDEX),
                                            resultSet.getString(2))
                    ));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<RoomDto> findById(int roomId) {
        String sql = "select * from room where id = :roomId";
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(sql, Map.of("roomId", roomId),
                            (resultSet, rowNum) ->
                                    new RoomDto(resultSet.getInt(KEY_INDEX), resultSet.getString(NAME_INDEX),
                                            resultSet.getString(3))
                    ));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void delete(int roomId) {
        String sql = "delete from room where id = :roomId";
        jdbcTemplate.update(sql, Map.of("roomId", roomId));
    }
}
