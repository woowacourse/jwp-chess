package wooteco.chess.dao;

import wooteco.chess.dto.Commands;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class MySqlChessDao implements ChessDao {
    @Override
    public void addCommand(Commands command) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public void setParameters(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, command.get());
            }
        };

        String sql = "INSERT INTO commands (command) VALUES (?)";
        jdbcTemplate.update(sql);
    }

    @Override
    public void clearCommands() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public void setParameters(PreparedStatement preparedStatement) throws SQLException {

            }
        };
        String sql = "TRUNCATE commands";
        jdbcTemplate.update(sql);
    }

    @Override
    public List<Commands> selectCommands() {
        SelectJdbcTemplate selectJdbcTemplate = new SelectJdbcTemplate() {
        };
        String sql = "SELECT * FROM commands";
        return selectJdbcTemplate.select(sql);
    }
}