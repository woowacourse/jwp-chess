package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dto.CreateGameRequest;
import chess.entity.GameEntity;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class GameDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private GameDao dao;

    @Test
    void saveAndGetGeneratedId_메서드는_저장한_데이터의_id값_반환() {
        CreateGameRequest request = new CreateGameRequest("title", "password");
        int actual = dao.saveAndGetGeneratedId(request);

        assertThat(actual).isEqualTo(3);
    }

    @Test
    void finishGame_메서드로_게임을_종료된_상태로_변경가능() {
        dao.finishGame(1);

        boolean actual = Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                "SELECT running FROM game WHERE id = 1", Boolean.class));

        assertThat(actual).isFalse();
    }

    @Test
    void countAll_메서드로_여태까지_저장된_모든_데이터의_개수_조회가능() {
        int actual = dao.countAll();

        assertThat(actual).isEqualTo(2);
    }

    @Test
    void countRunningGames_메서드로_running값이_참인_데이터의_개수_조회가능() {
        int actual = dao.countRunningGames();

        assertThat(actual).isEqualTo(1);
    }

    @Test
    void game_목록_전체_조회() {
        List<GameEntity> games = dao.selectAll();
        assertThat(games.size()).isEqualTo(2);
    }
}
