package chess.dao;

import chess.dao.entity.GameEntity;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcGameDao implements GameDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(GameEntity game) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            final String sql = "insert into game (title, password, turn, status) values (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(
                    sql,
                    new String[]{"id"}
            );
            preparedStatement.setString(1, game.getTitle());
            preparedStatement.setString(2, game.getPassword());
            preparedStatement.setString(3, game.getTurn());
            preparedStatement.setString(4, game.getStatus());
            return preparedStatement;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public void removeById(Long id) {
        final String sql = "delete from game where id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public GameEntity findGameById(Long id) {
        final String sql = "select * from game where id = ?";
        return jdbcTemplate.queryForObject(
                sql,
                (resultSet, rowNum) ->
                        new GameEntity(
                                resultSet.getLong("id"),
                                resultSet.getString("title"),
                                resultSet.getString("password"),
                                resultSet.getString("turn"),
                                resultSet.getString("status")
                        ),
                id
        );
    }

    @Override
    public String findPasswordById(Long id) {
        final String sql = "select password from game where id = ?";
        return jdbcTemplate.queryForObject(
                sql,
                (resultSet, rowNum) -> resultSet.getString("password"),
                id
        );
    }

    @Override
    public String findStatusById(Long id) {
        final String sql = "select status from game where id = ?";
        return jdbcTemplate.queryForObject(
                sql,
                (resultSet, rowNum) -> resultSet.getString("status"),
                id
        );
    }

    @Override
    public List<GameEntity> findAll() {
        final String sql = "select * from game";
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) ->
                        new GameEntity(
                                resultSet.getLong("id"),
                                resultSet.getString("title"),
                                resultSet.getString("password"),
                                resultSet.getString("turn"),
                                resultSet.getString("status")
                        )
        );
    }

    @Override
    public void updateGame(Long id, String turn, String status) {
        final String sql = "update game set turn = ?, status = ? where id = ?";
        jdbcTemplate.update(sql, turn, status, id);
    }

    @Override
    public void updateStatus(Long id, String status) {
        final String sql = "update game set status = ? where id = ?";
        jdbcTemplate.update(sql, status, id);
    }
}