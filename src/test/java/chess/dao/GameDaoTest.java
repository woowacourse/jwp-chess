package chess.dao;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import chess.domain.piece.Color;
import chess.dto.LogInDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class GameDaoTest {

    private static final LogInDto LOG_IN_DTO = new LogInDto("1234", "1234");
    private GameDao gameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        gameDao = new GameDao(jdbcTemplate);
    }

    @DisplayName("createById로 새 게임을 생성한다")
    @Test
    void createById() {
        gameDao.create(LOG_IN_DTO);
        assertThat(gameDao.isInId("1234")).isTrue();
    }

    @DisplayName("특정 아이디의 방이 없을 시 isInId가 false를 리턴한다")
    @Test
    void isInId() {
        gameDao.create(LOG_IN_DTO);
        assertThat(gameDao.isInId("123")).isFalse();
    }

    @DisplayName("게임이 끝나지 않으면 foce_end_flag는 false이다")
    @Test
    void findForceEndFlagById() {
        gameDao.create(LOG_IN_DTO);
        assertThat(gameDao.findForceEndFlag("1234")).isFalse();
    }

    @DisplayName("findForceEndFlagById에서 존재하지 않는 게임아이디 조회시 예외가 발생한다")
    @Test
    void findForceEndFlagByIdError() {
        gameDao.create(LOG_IN_DTO);
        assertThatThrownBy(() -> gameDao.findForceEndFlag("124"))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("findTurnById에서 첫 이동불가능한 turn은 black이다")
    @Test
    void findTurnById() {
        gameDao.create(LOG_IN_DTO);
        assertThat(gameDao.findTurn("1234")).isEqualTo(Color.BLACK);
    }

    @DisplayName("updateTurnById로 게임을 업데이트한다")
    @Test
    void updateTurnById() {
        gameDao.create(LOG_IN_DTO);
        gameDao.updateTurnById(Color.WHITE, "1234");
        assertThat(gameDao.findTurn("1234")).isEqualTo(Color.WHITE);
    }

    @DisplayName("updateForceEndFlagById를 통해 force_end_flag를 업데이트한다")
    @Test
    void updateForceEndFlagById() {
        gameDao.create(LOG_IN_DTO);
        gameDao.updateForceEndFlagById(true, "1234");
        assertThat(gameDao.findForceEndFlag("1234")).isEqualTo(true);
    }

    @DisplayName("deleteById로 게임을 삭제한다")
    @Test
    void deleteById() {
        gameDao.create(LOG_IN_DTO);
        assertThat(gameDao.isInId("1234")).isTrue();
        gameDao.deleteById("1234");
        assertThat(gameDao.isInId("1234")).isFalse();
    }

//    @DisplayName("isValidPassword로 올바른 패스워드인지 확인한다")
//    @Test
//    void isValidPassword() {
//
//    }
}
