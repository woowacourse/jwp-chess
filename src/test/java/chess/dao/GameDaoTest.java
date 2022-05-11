package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import chess.domain.Camp;
import chess.entity.GameEntity;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class GameDaoTest {
    @Autowired
    private GameDao gameDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final GameEntity gameEntity = new GameEntity("test", "pwd");

    @AfterEach
    void end() {
        jdbcTemplate.update("delete from GAME");
    }

    @DisplayName("새로운 게임을 저장한다.")
    @Test
    void save() {
        // given

        // when
        Long id = gameDao.save(gameEntity);

        // then
        GameEntity result = gameDao.findById(id);
        assertThat(result)
                .extracting("title", "password", "whiteTurn", "finished")
                .contains("test", "pwd", true, false);
    }

    @DisplayName("게임을 모두 조회한다.")
    @Test
    void findAll() {
        // given
        gameDao.save(gameEntity);
        gameDao.save(new GameEntity("test2", "pwd2"));

        // when
        List<GameEntity> result = gameDao.findAll();

        // then
        assertThat(result).hasSize(2)
                .extracting("title", "password")
                .containsExactly(
                        tuple("test", "pwd"),
                        tuple("test2", "pwd2")
                );

    }

    @DisplayName("id에 해당하는 게임을 조회한다.")
    @Test
    void findById() {
        // given
        Long savedId = gameDao.save(gameEntity);

        // when
        GameEntity result = gameDao.findById(savedId);

        // then
        assertThat(result)
                .extracting("title", "password")
                .containsExactly("test", "pwd");
    }

    @DisplayName("흑색 진영의 차례일 때 게임을 저장하고 조회하면 백색 진영의 차례가 아니다.")
    @Test
    void updateTurnById() {
        // given
        Long savedId = gameDao.save(gameEntity);
        Camp.initializeTurn();
        Camp.switchTurn();

        // when
        gameDao.updateTurnById(savedId);

        // then
        assertFalse(gameDao.isWhiteTurn(savedId));
    }

    @DisplayName("게임을 종료 상태로 수정한다.")
    @Test
    void updateStateById() {
        // given
        Long savedId = gameDao.save(gameEntity);

        // when
        gameDao.updateStateById(savedId);

        // then
        GameEntity result = gameDao.findById(savedId);
        assertThat(result)
                .extracting("finished")
                .isEqualTo(true);
    }

    @DisplayName("id에 해당하는 게임을 삭제한다.")
    @Test
    void deleteById() {
        // given
        Long savedId = gameDao.save(gameEntity);

        // when
        gameDao.deleteById(savedId);

        // then
        assertThatThrownBy(() -> gameDao.findById(savedId))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("백색 진영의 차례인 경우 true를 반환한다.")
    @Test
    void isWhiteTurn() {
        // given
        Long savedId = gameDao.save(gameEntity);

        // when
        boolean whiteTurn = gameDao.isWhiteTurn(savedId);

        // then
        assertTrue(whiteTurn);
    }
}
