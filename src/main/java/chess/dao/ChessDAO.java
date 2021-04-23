package chess.dao;

import chess.exception.NoDataExistenceException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChessDAO {

    private final JdbcTemplate jdbcTemplate;

    public ChessDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addChessGame(String gameId, String data) {
        String query = "INSERT INTO chess (game_id, data) VALUES (?, ?)";

        jdbcTemplate.update(query, gameId, data);
    }

    public void updateChessGame(String gameId, String data) {
        String query = "UPDATE chess SET data=? WHERE game_id = ?";

        jdbcTemplate.update(query, data, gameId);
    }

    public String findChessGameByGameId(String gameId) {
        String query = "SELECT data FROM chess WHERE game_id = ?";

        List<String> data = jdbcTemplate.query(query, (rs, i) -> rs.getString("data"), gameId);

        if (data.size() == 0) {
            throw new NoDataExistenceException();
        }

        return data.get(0);
    }

    public boolean isGameIdExisting(String gameId) {
        String query = "SELECT count(*) as count FROM chess WHERE game_id = ?";

        Integer count = jdbcTemplate.queryForObject(query, Integer.class, gameId);

        return count != 0;
    }

}
