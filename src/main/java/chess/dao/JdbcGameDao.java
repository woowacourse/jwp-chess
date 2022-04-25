package chess.dao;

import chess.dto.GameDto;
import chess.dto.GameStatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcGameDao implements GameDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void removeAll() {
        final String sql = "delete from game";
        jdbcTemplate.update(sql);
    }

    @Override
    public void save(GameDto gameDto) {
        final String sql = "insert into game (turn, status) values (?, ?)";
        jdbcTemplate.update(sql, gameDto.getTurn(), gameDto.getStatus());
    }

    @Override
    public void modify(GameDto gameDto) {
        final String sql = "update game set turn = ?, status = ?";
        jdbcTemplate.update(sql, gameDto.getTurn(), gameDto.getStatus());
    }

    @Override
    public void modifyStatus(GameStatusDto statusDto) {
        final String sql = "update game set status = ?";
        jdbcTemplate.update(sql, statusDto.getName());
    }

    @Override
    public GameDto find() {
        final String sql = "select * from game";
        try {
            return jdbcTemplate.queryForObject(sql, getGameDtoRowMapper());
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new GameDto(null, "ready");
        }
    }

    private RowMapper<GameDto> getGameDtoRowMapper() {
        return (resultSet, rowNum) ->
                new GameDto(
                        resultSet.getString("turn"),
                        resultSet.getString("status")
                );
    }
}