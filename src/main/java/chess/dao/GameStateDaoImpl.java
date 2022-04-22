package chess.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GameStateDaoImpl implements GameStateDao {

    private static final String DATABASE_EMPTY_SYMBOL = "nothing";

    private final JdbcTemplate jdbcTemplate;

    public GameStateDaoImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean hasPlayingGame() {
        final String sql = "select count(*) from game";
        final int count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count > 0;
    }

    @Override
    public void saveState(final String state) {
        String sql = "insert into game (state) values (?)";
        if (hasPlayingGame()) {
            sql = "update game set state = (?)";
        }
        jdbcTemplate.update(sql, state);
    }

    @Override
    public void saveTurn(final String turn) {
        String sql = "insert into game (turn) values (?)";
        if (hasPlayingGame()) {
            sql = "update game set turn = (?)";
        }
        jdbcTemplate.update(sql, turn);
    }

    @Override
    public String getGameState() {
        final String sql = "select state from game";
        if (hasPlayingGame()) {
            return jdbcTemplate.queryForObject(sql, String.class);
        }
        return DATABASE_EMPTY_SYMBOL;
    }

    @Override
    public String getTurn() {
        final String sql = "select turn from game";
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    @Override
    public void removeGameState() {
        final String sql = "delete from game";
        jdbcTemplate.update(sql);
    }
}
