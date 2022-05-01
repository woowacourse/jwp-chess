package chess.dao;

import chess.domain.CurrentStatus;
import chess.dto.CurrentStatusDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CurrentStatusDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private CurrentStatusDao dao;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM game");
        jdbcTemplate.execute("ALTER TABLE game AUTO_INCREMENT = 1");
    }

    @Test
    @DisplayName("gamId를 통해 올바른 현재 상태를 찾아오는지 확인")
    void findGameById() {
        jdbcTemplate.execute("INSERT INTO game(name,password) VALUES ('name','password')");
        jdbcTemplate.execute("INSERT INTO current_status(game_id,state,turn) VALUES (1,'READY', 'WHITE')");

        CurrentStatus currentStatus = dao.findByGameId(1);

        assertThat(currentStatus.getStateToString()).isEqualTo("READY");
        assertThat(currentStatus.getTurnToString()).isEqualTo("WHITE");
    }

    @Test
    @DisplayName("현재상태 저장 확인")
    void save() {
        jdbcTemplate.execute("INSERT INTO game(name,password) VALUES ('name','password')");

        CurrentStatus currentStatus = new CurrentStatus();
        dao.save(1, new CurrentStatusDto(currentStatus));

        assertThat(jdbcTemplate.queryForObject("SELECT state FROM current_status WHERE game_id=1", String.class)).isEqualTo("READY");
        assertThat(jdbcTemplate.queryForObject("SELECT turn FROM current_status WHERE game_id=1", String.class)).isEqualTo("WHITE");
    }

    @Test
    @DisplayName("현재상태 수정 확인")
    void update() {
        jdbcTemplate.execute("INSERT INTO game(name,password) VALUES ('name','password')");
        jdbcTemplate.execute("INSERT INTO current_status(game_id,state,turn) VALUES (1,'READY','WHITE')");

        CurrentStatus currentStatus = new CurrentStatus("PLAY", "BLACK");
        dao.update(1, new CurrentStatusDto(currentStatus));

        assertThat(jdbcTemplate.queryForObject("SELECT state FROM current_status WHERE game_id=1", String.class)).isEqualTo("PLAY");
        assertThat(jdbcTemplate.queryForObject("SELECT turn FROM current_status WHERE game_id=1", String.class)).isEqualTo("BLACK");
    }

    @Test
    @DisplayName("원하는상태로 수정하는지 확인")
    void saveState() {
        jdbcTemplate.execute("INSERT INTO game(name,password) VALUES ('name','password')");
        jdbcTemplate.execute("INSERT INTO current_status(game_id,state,turn) VALUES (1,'READY','WHITE')");

        dao.saveState(1, "FINISH");

        assertThat(jdbcTemplate.queryForObject("SELECT state FROM current_status WHERE game_id=1", String.class)).isEqualTo("FINISH");
    }

}
