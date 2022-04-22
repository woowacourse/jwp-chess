package chess.dao;

import chess.web.dto.RoomDto;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class RoomRepositoryImpl implements RoomRepository {

    private final SimpleJdbcInsert insertActor;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public RoomRepositoryImpl(DataSource dataSource,
                              NamedParameterJdbcTemplate jdbcTemplate) {
        this.insertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("room")
                .usingGeneratedKeyColumns("id");
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
                                new RoomDto(resultSet.getInt(1), resultSet.getString(2))
                )
        );
    }
}
