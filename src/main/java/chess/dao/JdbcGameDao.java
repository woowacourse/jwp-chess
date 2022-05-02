package chess.dao;

import chess.domain.entity.Game;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcGameDao implements GameDao {

    private static final String IS_EXIST_GAME_DML = "select exists (select game_id from games limit 1 ) as `exists`";
    private static final String FIND_LAST_GAME_DML = "select * from games order by create_at desc limit 1";
    private static final String SAVE_DML = "insert into games (game_id, last_team, create_at) values (?, ?, ?)";
    private static final String CREATE_DML = "insert into games (game_id, room_name, room_encrypted_password, last_team, create_at) values (?, ?, ?, ?, ?)";

    private static final String DELETE_ALL_DML = "delete from games";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean isExistGame() {
        return jdbcTemplate.queryForObject(IS_EXIST_GAME_DML, Boolean.class);
    }

    @Override
    public void save(String gameId, String lastTeamName, LocalDateTime createdAt) {
        jdbcTemplate.update(SAVE_DML, gameId, lastTeamName, createdAt);
    }

    @Override
    public void createGame(String gameId, String roomName, String roomEncryptedPassword, String teamName, LocalDateTime createdAt) {
        jdbcTemplate.update(CREATE_DML, gameId, roomName, roomEncryptedPassword, teamName, createdAt);
    }

    @Override
    public Game findLastGame() {
        return jdbcTemplate.queryForObject(FIND_LAST_GAME_DML,
                (rs, rowNum) -> new Game(
                        rs.getString("game_id"),
                        rs.getString("last_team"),
                        rs.getTimestamp("create_at")
                )
        );
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(DELETE_ALL_DML);
    }
}
