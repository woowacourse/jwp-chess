package chess.dao;

import chess.dto.GameDto;
import chess.dto.GameStatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GameDaoImpl implements GameDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GameDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void removeAll() {
        final String sql = "delete from game";
        jdbcTemplate.update(sql);
    }

    @Override
    public void saveGame(GameDto gameDto) {
        final String sql = "insert into game (turn, status) values (?, ?)";
        jdbcTemplate.update(sql, gameDto.getTurn(), gameDto.getStatus());
    }

    @Override
    public void updateGame(GameDto gameDto) {
        final String sql = "update game set turn = ?, status = ?";
        jdbcTemplate.update(sql, gameDto.getTurn(), gameDto.getStatus());
    }

    @Override
    public void updateStatus(GameStatusDto statusDto) {
        final String sql = "update game set status = ?";
        jdbcTemplate.update(sql, statusDto.getName());
    }

    @Override
    public GameDto findGame() {
        final String sql = "select * from game";
        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    (resultSet, rowNum) ->
                            new GameDto(
                                    resultSet.getString("turn"),
                                    resultSet.getString("status")
                            )
            );
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new GameDto(null, "ready");
        }
    }
}
