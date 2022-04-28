package chess.repository;

import chess.entity.CommandEntity;
import chess.entity.RoomEntity;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
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

    public List<String> findAll() {
        String sql = "select * from command";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            return resultSet.getString("command");
        });
    }

    public void clear() {
        String sql = "truncate table command";
        jdbcTemplate.update(sql);
    }

    public CommandEntity insert(CommandEntity commandEntity) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(commandEntity);
        insertActor.executeAndReturnKey(parameters).longValue();
        return new CommandEntity(commandEntity.getRoomId(), commandEntity.getCommand());
    }
}
