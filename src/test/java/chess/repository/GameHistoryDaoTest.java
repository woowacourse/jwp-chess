package chess.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import chess.domain.web.GameHistory;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GameHistoryDaoTest {

    private static final int DEFAULT_GAME_ID = 1;

    @Autowired
    private GameHistoryDao gameHistoryDao;

    @BeforeAll
    public void setGameHistoryData() {
        GameHistory gameHistory1 = new GameHistory(DEFAULT_GAME_ID, "move a2 a4",
            LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        GameHistory gameHistory2 = new GameHistory(DEFAULT_GAME_ID, "move a2 a4",
            LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        GameHistory gameHistory3 = new GameHistory(DEFAULT_GAME_ID, "move a2 a4",
            LocalDateTime.now(ZoneId.of("Asia/Seoul")));

        gameHistoryDao.addGameHistory(gameHistory1);
        gameHistoryDao.addGameHistory(gameHistory2);
        gameHistoryDao.addGameHistory(gameHistory3);
    }

    @Test
    @DisplayName("게임 history 등록 테스트")
    public void addGameTest() {
        GameHistory gameHistory = new GameHistory(DEFAULT_GAME_ID, "move a2 a4",
            LocalDateTime.now(ZoneId.of("Asia/Seoul")));

        assertThatCode(() -> gameHistoryDao.addGameHistory(gameHistory)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("game 아이디로 game이 가지고 있는 게임 history 리스트 찾기")
    public void findGamesByIdTest() {
        List<GameHistory> allGameHistoryByGameId = gameHistoryDao
            .findAllGameHistoryByGameId(DEFAULT_GAME_ID);

        assertThat(allGameHistoryByGameId.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("game 아이디로 game이 가지고 있는 게임 history 리스트 찾기 - 빈 리스트")
    public void findGamesByIdEmptyTest() {
        List<GameHistory> allGameHistoryByGameId = gameHistoryDao
            .findAllGameHistoryByGameId(2);

        assertThat(allGameHistoryByGameId.isEmpty()).isTrue();
    }

}