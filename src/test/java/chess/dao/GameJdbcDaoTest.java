package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.piece.Team;
import chess.dto.GameDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GameJdbcDaoTest {

    @Autowired
    private GameDao gameDao;

    private int gameId;

    @BeforeEach
    void init() {
        gameId = gameDao.save(new GameDto("a", "b", "WHITE"));
    }

    @AfterEach
    void clear() {
        gameDao.deleteById(gameId);
    }

    @Test
    @DisplayName("아이디로 게임 정보를 찾는다.")
    void findById() {
        GameDto gameDto = gameDao.findGames().get(0);

        assertThat(gameDto.getRoomName()).isEqualTo("a");
    }

    @Test
    @DisplayName("게임의 상태를 업데이트한다.")
    void update() {
        gameDao.update(Team.BLACK.name(), gameId);
        GameDto gameDto = gameDao.findGames().get(0);

        assertThat(gameDto.getState()).isEqualTo(Team.BLACK.name());
    }
}
