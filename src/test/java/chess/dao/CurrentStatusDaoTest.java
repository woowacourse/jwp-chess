package chess.dao;

import chess.domain.CurrentStatus;
import chess.dto.CurrentStatusDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CurrentStatusDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private CurrentStatusDao dao;

    @AfterEach()
    void clear() {
        jdbcTemplate.execute("DELETE FROM game");
    }

    @Test
    @DisplayName("gamId를 통해 올바른 현재 상태를 찾아오는지 확인")
    void findGameById() {
        int id = insertAndGetId();
        jdbcTemplate.update("INSERT INTO current_status(game_id,state,turn) VALUES (?,'READY', 'WHITE')", id);

        CurrentStatus currentStatus = dao.findByGameId(id);

        assertThat(currentStatus.getStateToString()).isEqualTo("READY");
        assertThat(currentStatus.getTurnToString()).isEqualTo("WHITE");
    }

    @Test
    @DisplayName("현재상태 저장 확인")
    void save() {
        int id = insertAndGetId();
        CurrentStatus currentStatus = new CurrentStatus();
        dao.save(id, new CurrentStatusDto(currentStatus));

        assertThat(jdbcTemplate.queryForObject("SELECT state FROM current_status WHERE game_id=?", String.class, id)).isEqualTo("READY");
        assertThat(jdbcTemplate.queryForObject("SELECT turn FROM current_status WHERE game_id=?", String.class, id)).isEqualTo("WHITE");
    }

    @Test
    @DisplayName("현재상태 수정 확인")
    void update() {
        int id = insertAndGetId();
        jdbcTemplate.update("INSERT INTO current_status(game_id,state,turn) VALUES (?,'READY','WHITE')", id);

        CurrentStatus currentStatus = new CurrentStatus("PLAY", "BLACK");
        dao.update(id, new CurrentStatusDto(currentStatus));

        assertThat(jdbcTemplate.queryForObject("SELECT state FROM current_status WHERE game_id=?", String.class, id)).isEqualTo("PLAY");
        assertThat(jdbcTemplate.queryForObject("SELECT turn FROM current_status WHERE game_id=?", String.class, id)).isEqualTo("BLACK");
    }

    @Test
    @DisplayName("원하는상태로 수정하는지 확인")
    void saveState() {
        int id = insertAndGetId();
        jdbcTemplate.update("INSERT INTO current_status(game_id,state,turn) VALUES (?,'READY','WHITE')", id);

        dao.saveState(id, "FINISH");

        assertThat(jdbcTemplate.queryForObject("SELECT state FROM current_status WHERE game_id=?", String.class, id)).isEqualTo("FINISH");
    }

    private int insertAndGetId() {
        final String sql = "INSERT INTO game(name,password) VALUES ('name','password')";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

}
