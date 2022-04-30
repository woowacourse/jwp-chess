package chess.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JdbcGameStateDao implements GameStateDao {

    private static final String DATABASE_EMPTY_SYMBOL = "nothing";

    private final JdbcTemplate jdbcTemplate;

    public JdbcGameStateDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean hasPlayingGame(final int roomNumber) {
        final String sql = "select count(*) from game where roomnumber=?";
        final int count = jdbcTemplate.queryForObject(sql, Integer.class, roomNumber);
        return count > 0;
    }

    @Override
    public void saveState(final int roomNumber, final String state) {
        String sql = "insert into game(state, roomNumber) values (?, ?)";
        if (hasPlayingGame(roomNumber)) {
            sql = "update game set state=(?) where roomnumber=?";
        }
        jdbcTemplate.update(sql, state, roomNumber);
    }

    @Override
    public void saveTurn(final int roomNumber, final String turn) {
        String sql = "insert into game(turn, roomnumber) values (?, ?)";
        if (hasPlayingGame(roomNumber)) {
            sql = "update game set turn=(?) where roomnumber=?";
        }
        jdbcTemplate.update(sql, turn, roomNumber);
    }

    @Override
    public String getGameState(final int roomNumber) {
        final String sql = "select state from game where roomNumber=?";
        if (hasPlayingGame(roomNumber)) {
            return jdbcTemplate.queryForObject(sql, String.class, roomNumber);
        }
        return DATABASE_EMPTY_SYMBOL;
    }

    @Override
    public String getTurn(final int roomNumber) {
        final String sql = "select turn from game where roomnumber=?";
        return jdbcTemplate.queryForObject(sql, String.class, roomNumber);
    }

    @Override
    public void removeGameState(final int roomNumber) {
        final String sql = "delete from game where roomnumber=?";
        jdbcTemplate.update(sql, roomNumber);
    }
}
