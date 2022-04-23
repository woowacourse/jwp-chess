package chess.repository;

import chess.web.dto.RoomDto;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class RoomRepositoryImpl implements RoomRepository {

    private static final String TABLE_NAME = "room";
    private static final String KEY_NAME = "id";
    private static final int KEY_INDEX = 1;
    private static final int NAME_INDEX = 2;

    private final SimpleJdbcInsert insertActor;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public RoomRepositoryImpl(DataSource dataSource,
                              NamedParameterJdbcTemplate jdbcTemplate) {
        this.insertActor = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_NAME);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(String name) {
        return insertActor.executeAndReturnKey(Map.of("name", name)).intValue();
    }

    @Override
    public Optional<RoomDto> find(String name) {
        String sql = "select * from room where name = :name";
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(sql, Map.of("name", name),
                        (resultSet, rowNum) ->
                                new RoomDto(resultSet.getInt(KEY_INDEX), resultSet.getString(NAME_INDEX))
                )
        );
    }
}
