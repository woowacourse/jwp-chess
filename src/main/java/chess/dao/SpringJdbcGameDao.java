package chess.dao;

import chess.dto.GameStatusDto;
import chess.domain.GameStatus;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SpringJdbcGameDao implements GameDao {

    private final JdbcTemplate jdbcTemplate;

    public SpringJdbcGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void removeAll() {
        final String sql = "delete from game";
        jdbcTemplate.update(sql);
    }

    @Override
    public void saveGame(GameStatusDto gameDto) {
        final String sql = "insert into game (turn, status) values (?, ?)";
        jdbcTemplate.update(sql, gameDto.getTurn(), gameDto.getStatus());
    }

    @Override
    public void updateGame(GameStatusDto gameDto) {
        final String sql = "update game set turn = ?, status = ?";
        jdbcTemplate.update(sql, gameDto.getTurn(), gameDto.getStatus());
    }

    @Override
    public void updateStatus(GameStatus statusDto) {
        final String sql = "update game set status = ?";
        jdbcTemplate.update(sql, statusDto.getName());
    }

    @Override
    public GameStatusDto findGame() {
        final String sql = "select * from game";
        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    (resultSet, rowNum) ->
                            new GameStatusDto(
                                    resultSet.getString("turn"),
                                    resultSet.getString("status")
                            )
            );
        } catch (EmptyResultDataAccessException e) {
            return new GameStatusDto(null, "ready");
        }
    }
}
