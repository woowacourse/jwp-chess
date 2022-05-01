package chess.dao;

import static chess.util.RandomCreationUtils.createUuid;
import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.entity.Game;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JdbcGameDaoTest {

    @Autowired
    private JdbcGameDao gameDao;

    @AfterEach
    void tearDown() {
        gameDao.deleteAll();
    }

    @DisplayName("게임이 존재하는 지 여부 확인 테스트")
    @Test
    void existGame() {
        boolean existGameBeforeSave = gameDao.isExistGame();
        assertThat(existGameBeforeSave).isFalse();
        gameDao.save(createUuid(), "Black Team", LocalDateTime.now());
    }

    @DisplayName("가장 마지막 게임을 찾는지 테스트")
    @Test
    void findLastGame() {
        String lastGameId = createUuid();
        gameDao.save(createUuid(), "Black Team", LocalDateTime.of(2022, 05, 01, 0, 0));
        gameDao.save(lastGameId, "Black Team", LocalDateTime.of(2022, 05, 03, 0, 0));
        gameDao.save(createUuid(), "Black Team", LocalDateTime.of(2022, 05, 02, 0, 0));

        Game findLastGame = gameDao.findLastGame();
        assertThat(findLastGame.getGameId()).isEqualTo(lastGameId);
    }
}