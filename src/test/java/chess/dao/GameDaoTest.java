package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;

import chess.domain.Camp;
import chess.dto.GameDto;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GameDaoTest {
    @Autowired
    public GameDao gameDao;

    @DisplayName("기존에 저장된 game이 있어도 data를 덮어쓸 수 있다.")
    @Test
    void save_twice() {
        gameDao.insert(GameDto.fromNewGame("방제목1", "password"));
        assertThatNoException().isThrownBy(() -> gameDao.update(1, true));
    }

    @DisplayName("초기 상태의 게임을 저장하고 불러오면 백색 진영의 차례이다.")
    @Test
    void isWhiteTurn_false() {
        gameDao.insert(GameDto.fromNewGame("방제목1", "password"));

        assertThat(gameDao.isWhiteTurn(1)).isTrue();
    }

    @DisplayName("게임 목록을 조회할 수 있다.")
    @Test
    void selectGames() {
        gameDao.insert(GameDto.fromNewGame("방제목1", "password"));
        List<GameDto> games = gameDao.selectGames();
        assertThat(games.get(0).getTitle()).isEqualTo("방제목1");
    }
}
