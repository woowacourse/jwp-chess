package chess.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import chess.domain.web.Game;
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
class GameDaoTest {

    private static final int DEFAULT_HAS_GAME_USER_ID = 1;

    @Autowired
    private GameDao gameDao;

    @BeforeAll
    public void setUserData() {
        Game game1 = new Game(DEFAULT_HAS_GAME_USER_ID, false,
            LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        Game game2 = new Game(DEFAULT_HAS_GAME_USER_ID, false,
            LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        Game game3 = new Game(DEFAULT_HAS_GAME_USER_ID, false,
            LocalDateTime.now(ZoneId.of("Asia/Seoul")));

        gameDao.addGame(game1);
        gameDao.addGame(game2);
        gameDao.addGame(game3);
    }

    @Test
    @DisplayName("게임 등록 테스트")
    public void addGameTest() {
        Game game = new Game(DEFAULT_HAS_GAME_USER_ID, false,
            LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        int gameId = gameDao.addGame(game);

        assertThat(gameId).isEqualTo(4);
    }

    @Test
    @DisplayName("user 아이디로 user가 속해 있는 게임 리스트 찾기")
    public void findGamesByIdTest() {
        List<Game> gamesByUserId = gameDao.findGamesByUserId(DEFAULT_HAS_GAME_USER_ID);

        assertThat(gamesByUserId.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("user 아이디로 user가 속해 있는 게임 리스트 찾기 - 빈 리스트")
    public void findGamesByIdEmptyTest() {
        List<Game> gamesByUserId = gameDao.findGamesByUserId(2);

        assertThat(gamesByUserId.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("게임 아이디로 게임 상태 업데이트")
    public void findUserByNameTest() {
        int updateGameId = 1;

        assertThatCode(() -> gameDao.updateGameIsEnd(updateGameId)).doesNotThrowAnyException();
    }
}
