package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;

import chess.domain.Camp;
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
        gameDao.save();
        assertThatNoException().isThrownBy(gameDao::save);
    }

    @DisplayName("흑색 진영의 차례일 때 게임을 저장하고 불러오면 백색 진영의 차례가 아니다.")
    @Test
    void isWhiteTurn_false() {
        Camp.initializeTurn();
        Camp.switchTurn();
        gameDao.save();

        assertThat(gameDao.isWhiteTurn()).isFalse();
    }
}
