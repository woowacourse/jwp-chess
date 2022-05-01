package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;

import chess.domain.Camp;
import chess.domain.ChessGame;
import chess.dto.GameDto;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

@SpringBootTest
class GameDaoTest {
    @Autowired
    private GameDao gameDao;
    private ChessGame chessGame;
    private int id;

    @BeforeEach
    void before() {
        chessGame = new ChessGame("test", "test");
        id = gameDao.save(chessGame);
    }

    @AfterEach
    void after() {
        gameDao.deleteById(id);
    }

    @DisplayName("새로운 게임이 저장되었는지 테스트한다.")
    @Test
    void create() {
        ChessGame expected = gameDao.findById(id);

        assertThat(chessGame).isEqualTo(expected);
    }

    @DisplayName("저장된 모든 게임을 조회한다.")
    @Test
    void findAll() {
        List<GameDto> result = gameDao.findAll();
        assertThat(result).isNotEmpty();
    }

    @DisplayName("기존에 저장된 game이 있어도 data를 덮어쓸 수 있다.")
    @Test
    void save_twice() {
        assertThatNoException().isThrownBy(() -> gameDao.updateTurnById(id));
    }

    @DisplayName("흑색 진영의 차례일 때 게임을 저장하고 불러오면 백색 진영의 차례가 아니다.")
    @Test
    void isWhiteTurn_false() {
        Camp.initializeTurn();
        Camp.switchTurn();
        gameDao.updateTurnById(id);

        assertThat(gameDao.isWhiteTurn(id)).isFalse();
    }

    @DisplayName("id에 해당하는 게임을 삭제한다.")
    @Test
    void deleteById() {
        int id = gameDao.save(new ChessGame("test", "test"));
        gameDao.deleteById(id);

        assertThatThrownBy(() -> gameDao.findById(id))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
