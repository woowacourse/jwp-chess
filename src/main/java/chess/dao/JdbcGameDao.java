package chess.dao;

import chess.dao.dto.GameDto;
import chess.domain.GameStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
public class JdbcGameDao implements GameDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(GameDto gameDto) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            final String sql = "insert into game (title, password, turn, status) values (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(
                    sql,
                    new String[]{"id"}
            );
            preparedStatement.setString(1, gameDto.getTitle());
            preparedStatement.setString(2, gameDto.getPassword());
            preparedStatement.setString(3, gameDto.getTurn());
            preparedStatement.setString(4, gameDto.getStatus());
            return preparedStatement;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public void removeById(Long gameId) {
        final String sql = "delete from game where id = ?";
        jdbcTemplate.update(sql, gameId);
    }

    @Override
    public GameDto findGameById(Long id) {
        final String sql = "select * from game where id = ?";
        return jdbcTemplate.queryForObject(
                sql,
                (resultSet, rowNum) ->
                        new GameDto(
                                resultSet.getLong("id"),
                                resultSet.getString("title"),
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
    public GameStatus findStatusById(Long id) {
        final String sql = "select status from game where id = ?";
        return jdbcTemplate.queryForObject(
                sql,
                (resultSet, rowNum) -> GameStatus.find(resultSet.getString("status")),
                id
        );
    }

    @Override
    public List<GameDto> findAll() {
        final String sql = "select * from game";
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) ->
                        new GameDto(
                                resultSet.getLong("id"),
                                resultSet.getString("title"),
                                resultSet.getString("turn"),
                                resultSet.getString("status")
                        )
        );
    }

    @Override
    public void updateGame(Long gameId, String turn, String status) {
        final String sql = "update game set turn = ?, status = ? where id = ?";
        jdbcTemplate.update(sql, turn, status, gameId);
    }

    @Override
    public void updateStatus(Long gameId, GameStatus statusDto) {
        final String sql = "update game set status = ? where id = ?";
        jdbcTemplate.update(sql, statusDto.getName(), gameId);
    }
}