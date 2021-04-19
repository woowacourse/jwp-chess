package chess.dao.jdbctemplate;

import chess.dao.GameDao;
import chess.dao.HistoryDao;
import chess.dao.dto.history.HistoryDto;
import chess.domain.game.Game;
import chess.domain.history.History;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@DataJdbcTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@TestPropertySource("classpath:application-test.properties")
class HistoryDaoTemplateTest {

    private final JdbcTemplate jdbcTemplate;
    private final HistoryDao historyDao;
    private final GameDao gameDao;

    @Autowired
    public HistoryDaoTemplateTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        historyDao = new HistoryDaoTemplate(jdbcTemplate);
        gameDao = new GameDaoTemplate(jdbcTemplate);
    }

    @BeforeEach
    void setUp() {
        gameDao.saveGame(Game.of("게임1", "흰색유저1", "흑색유저1"));
        String sql = "INSERT INTO history(game_id, move_command, turn_owner, turn_number, playing) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, 1, "move a1, a2", "WHITE", 1, true);
        jdbcTemplate.update(sql, 1, "move a2, a3", "BLACK", 1, true);
        jdbcTemplate.update(sql, 1, "move a3, a4", "WHITE", 2, true);
        jdbcTemplate.update(sql, 1, "move a4, a5", "BLACK", 2, true);
        jdbcTemplate.execute("COMMIT");
    }

    @AfterEach
    void afterTest() {
        jdbcTemplate.execute("DELETE FROM history");
        jdbcTemplate.execute("COMMIT");
    }

    @Test
    @DisplayName("체스 게임방의 진행 기록 저장된다.")
    void shouldSaveHistory() {
        //given
        Long gameId = 1L;
        String moveCommand = "move a5 a6";
        String turnOwner = "WHITE";
        int turnNumber = 3;
        boolean isPlaying = true;
        History history = History.of(moveCommand, turnOwner, turnNumber, isPlaying);

        //when
        historyDao.saveHistory(history, gameId);
        List<HistoryDto> game = historyDao.findHistoryByGameId(gameId);

        //then
        assertThat(game).isNotEmpty();
        assertThat(game).hasSize(5);
    }

    @Test
    @DisplayName("체스 게임방의 전체 기록 검색해서 반환한다.")
    void shouldFindHistoryByGameId() {
        //given
        Long gameId = 1L;
        List<HistoryDto> historyResponseDtos = Arrays.asList(
                new HistoryDto("move a1, a2", "WHITE", 1, true),
                new HistoryDto("move a2, a3", "BLACK", 1, true),
                new HistoryDto("move a3, a4", "WHITE", 2, true),
                new HistoryDto("move a4, a5", "BLACK", 2, true)
        );
        //when
        List<HistoryDto> findHistories = historyDao.findHistoryByGameId(gameId);

        //then
        assertThat(findHistories).hasSize(4);
        for (int i = 0; i < findHistories.size(); i++) {
            assertThat(findHistories.get(i).getMoveCommand()).isEqualTo(historyResponseDtos.get(i).getMoveCommand());
            assertThat(findHistories.get(i).getTurnOwner()).isEqualTo(historyResponseDtos.get(i).getTurnOwner());
            assertThat(findHistories.get(i).getTurnNumber()).isEqualTo(historyResponseDtos.get(i).getTurnNumber());
            assertThat(findHistories.get(i).isPlaying()).isEqualTo(historyResponseDtos.get(i).isPlaying());
        }
    }
}
