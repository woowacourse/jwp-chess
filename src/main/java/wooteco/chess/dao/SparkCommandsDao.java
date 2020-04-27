package wooteco.chess.dao;

import wooteco.chess.dto.Commands;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public abstract class SparkCommandsDao implements ChessDao {
    @Override
    public Commands save(Commands command) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public void setParameters(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, command.get());
            }
        };
        String sql = "INSERT INTO commands (command) VALUES (?)";
        jdbcTemplate.update(sql);

        return command;
    }

    @Override
    public void deleteAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public void setParameters(PreparedStatement preparedStatement) throws SQLException {
            }
        };
        String sql = "TRUNCATE commands";
        jdbcTemplate.update(sql);
    }

    @Override
    public <S extends Commands> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Commands> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<Commands> findAll() {
        SelectJdbcTemplate selectJdbcTemplate = new SelectJdbcTemplate() {
        };
        String sql = "SELECT * FROM commands";
        return selectJdbcTemplate.select(sql);
    }

    @Override
    public Iterable<Commands> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Commands entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends Commands> entities) {

    }
}
