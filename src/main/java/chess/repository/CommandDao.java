package chess.repository;

import chess.entity.CommandEntity;
import chess.entity.RoomEntity;
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

    public void insert(String command) {
        String sql = "insert into command (command) values (?)";
        jdbcTemplate.update(sql, command);
    }

    public CommandEntity insert(CommandEntity commandEntity) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(commandEntity);
        final long commandId = insertActor.executeAndReturnKey(parameters).longValue();
        return new CommandEntity(commandId, commandEntity.getRoomId(), commandEntity.getCommand());
    }

    public List<CommandEntity> findAll() {
        String sql = "select * from commands";
        return jdbcTemplate.query(sql, rowMapper());
    }

    public List<String> findAllInCommandTable() {
        String sql = "select * from command";
        return jdbcTemplate.query(sql, stringRowMapper());
    }

    private RowMapper<String> stringRowMapper() {
        return (rs, rowNum) -> {
            final String command = rs.getString("command");
            return command;
        };
    }

    private RowMapper<CommandEntity> rowMapper() {
        return (rs, rowNum) -> {
            final Long commandId = rs.getLong("command_id");
            final Long roomId = rs.getLong("room_id");
            final String command = rs.getString("command");
            return new CommandEntity(commandId, roomId, command);
        };
    }

    public void clear() {
        String sql = "truncate table command";
        jdbcTemplate.update(sql);
    }
}
