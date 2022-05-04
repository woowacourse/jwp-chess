package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.entity.Game;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class GameDaoTest {

    @Autowired
    private GameDao gameDao;

    private int gameId;

    @BeforeEach
    void setUp() {
        gameId = gameDao.save(new Game("title", "password", "WhiteTurn"));
    }

    @AfterEach
    void clear() {
        gameDao.delete(gameId);
    }

    @Test
    @DisplayName("게임들의 리스트를 조회할 수 있다.")
    void findAll() {
        List<Game> games = gameDao.findAll();

        assertThat(games.get(0).getTitle()).isEqualTo("title");
    }

    @Test
    @DisplayName("게임을 조회할 수 있다.")
    void findById() {
        Game game = gameDao.findById(gameId);

        assertThat(game.getPassword()).isEqualTo("password");
    }

    @Test
    @DisplayName("게임의 상태를 업데이트할 수 있다.")
    void update() {
        gameDao.update(new Game("BlackTurn", gameId));
        Game game = gameDao.findById(gameId);
        String state = game.getState();

        assertThat(state).isEqualTo("BlackTurn");
    }
}
