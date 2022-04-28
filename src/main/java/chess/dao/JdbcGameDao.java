package chess.dao;

import chess.dao.dto.GameDto;
import chess.dto.GameStatusDto;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
public class JdbcGameDao implements SpringGameDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long save(GameDto gameDto) {
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
    public void remove(GameDto gameDto) {
        final String sql = "delete from game where id = ? and password = ?";
        try {
            jdbcTemplate.update(sql, gameDto.getId(), gameDto.getPassword());
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("게임을 삭제할 수 없습니다.");
        }
    }

    @Override
    public GameDto findById(Long id) {
        final String sql = "select * from game where id = ?";
        try {
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
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("게임(id=" + id + ")을 찾을 수 없습니다.");
        }
    }

    @Override
    public List<GameDto> findAll() {
        final String sql = "select * from game";
        try {
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
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("존재하는 게임이 없습니다.");
        }
    }

    @Override
    public void updateGame(GameDto gameDto) {
        final String sql = "update game set turn = ?, status = ? where id = ?";
        jdbcTemplate.update(sql, gameDto.getTurn(), gameDto.getStatus(), gameDto.getId());
    }

    @Override
    public void updateStatus(Long gameId, GameStatusDto statusDto) {
        final String sql = "update game set status = ? where id = ?";
        jdbcTemplate.update(sql, statusDto.getName(), gameId);
    }
}