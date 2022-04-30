package chess.repository;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dto.GameDto;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class GameDaoTest {

    @Autowired
    private GameDao gameDao;

    private int gameId;

    @BeforeEach
    void setUp() {
        gameId = gameDao.save("title", "password", "WhiteRunning");
    }

    @AfterEach
    void clear() {
        gameDao.delete(gameId);
    }

    @Test
    @DisplayName("게임들의 리스트를 조회할 수 있다.")
    void findAll() {
        List<GameDto> games = gameDao.findAll();

        assertThat(games.get(0).getTitle()).isEqualTo("title");
    }

    @Test
    @DisplayName("게임의 상태를 조회할 수 있다.")
    void findState() {
        String state = gameDao.findState(gameId).toString();

        assertThat(state).isEqualTo("WhiteRunning");
    }

    @Test
    @DisplayName("게임의 비밀번호를 조회할 수 있다.")
    void findPassword() {
        String password = gameDao.findPassword(gameId);

        assertThat(password).isEqualTo("password");
    }

    @Test
    @DisplayName("게임의 갯수를 조회할 수 있다.")
    void findGameCount() {
        Long gameCount = gameDao.findGameCount();

        assertThat(gameCount).isEqualTo(1);
    }

    @Test
    @DisplayName("게임의 상태를 업데이트할 수 있다.")
    void update() {
        gameDao.update("BlackRunning", gameId);
        String state = gameDao.findState(gameId).toString();

        assertThat(state).isEqualTo("BlackRunning");
    }
}
