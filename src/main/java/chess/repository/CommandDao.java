package chess.repository;

import chess.entity.CommandEntity;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommandDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public CommandDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("commands")
                .usingGeneratedKeyColumns("command_id");
    }

    public CommandEntity insert(CommandEntity commandEntity) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(commandEntity);
        final long commandId = insertActor.executeAndReturnKey(parameters).longValue();
        return new CommandEntity(commandId, commandEntity.getRoomId(), commandEntity.getCommand());
    }

    public List<CommandEntity> findAllByRoomId(Long id) {
        String sql = "select * from commands where room_id = ?";
        return jdbcTemplate.query(sql, rowMapper(), id);
    }

    private RowMapper<CommandEntity> rowMapper() {
        return (rs, rowNum) -> {
            final Long commandId = rs.getLong("command_id");
            final Long roomId = rs.getLong("room_id");
            final String command = rs.getString("command");
            return new CommandEntity(commandId, roomId, command);
        };
    }
}
