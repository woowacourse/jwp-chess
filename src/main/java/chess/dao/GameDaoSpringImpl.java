package chess.dao;

import chess.dto.GameDto;
import chess.dto.GameStatusDto;
import org.springframework.jdbc.core.JdbcTemplate;

public class GameDaoSpringImpl implements GameDao {

    private JdbcTemplate jdbcTemplate;

    public GameDaoSpringImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void removeAll() {
        final String sql = "delete from game";
        jdbcTemplate.update(sql);
    }

    @Override
    public void save(GameDto gameDto) {

    }

    @Override
    public void update(GameDto gameDto) {

    }

    @Override
    public void updateStatus(GameStatusDto statusDto) {

    }

    @Override
    public GameDto find() {
        return null;
    }
}
