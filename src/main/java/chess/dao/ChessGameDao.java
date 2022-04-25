package chess.dao;

import java.time.LocalDateTime;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChessGameDao {

    private static final String isExistGameDml = "select exists (select chess_game_id from chess_games limit 1 ) as `exists`";
    private static final String saveDml = "insert into chess_games (chess_game_id, last_team, create_at) values (?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;

    public ChessGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean isExistGame() {
        return jdbcTemplate.queryForObject(isExistGameDml, Boolean.class);
    }

    public void save(String gameId, String lastTeamName) {
        LocalDateTime createdAt = LocalDateTime.now();
        jdbcTemplate.update(saveDml, gameId, lastTeamName, createdAt);
    }
}
