package chess.dao;

import chess.domain.db.Game;
import java.time.LocalDateTime;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GameDao {

    private static final String isExistGameDml = "select exists (select game_id from games limit 1 ) as `exists`";
    private static final String findLastGameDml = "select * from games order by create_at desc limit 1";
    private static final String saveDml = "insert into games (game_id, last_team, create_at) values (?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;

    public GameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean isExistGame() {
        return jdbcTemplate.queryForObject(isExistGameDml, Boolean.class);
    }

    public void save(String gameId, String lastTeamName) {
        LocalDateTime createdAt = LocalDateTime.now();
        jdbcTemplate.update(saveDml, gameId, lastTeamName, createdAt);
    }

    public Game findLastGame() {
        return jdbcTemplate.queryForObject(findLastGameDml,
                (rs, rowNum) -> new Game(
                        rs.getString("game_id"),
                        rs.getString("last_team"),
                        rs.getTimestamp("create_at")
                )
        );
    }
}
