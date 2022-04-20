package chess.dao;

import chess.dto.GameDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GameJdbcDao implements GameDao {

    private final JdbcTemplate jdbcTemplate;

    public GameJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(GameDto gameDto) {
        final String sql = "insert into game (white_user_name, black_user_name, state) values (?, ?, ?)";
        jdbcTemplate.update(sql, gameDto.getWhiteUserName(), gameDto.getBlackUserName(), gameDto.getState());
    }

    @Override
    public int findGameIdByUserName(String whiteUserName, String blackUserName) {
        final String sql = "select ifnull((select game.id from game where white_user_name = ? and black_user_name = ?), 0) from dual";
        return jdbcTemplate.queryForObject(sql, Integer.class, whiteUserName, blackUserName);
    }

    @Override
    public GameDto findById(int gameId) {
        final String sql = "select * from game where id = (?)";
        return jdbcTemplate.queryForObject(sql, GameDto.class, gameId);
    }

    @Override
    public void update(String state, int gameId) {
        final String sql = "update game set state = (?) where id = (?)";
        jdbcTemplate.update(sql, state, gameId);
    }

    @Override
    public void deleteById(int gameId) {
        final String sql = "delete from game where id = (?)";
        jdbcTemplate.update(sql, gameId);
    }
}
