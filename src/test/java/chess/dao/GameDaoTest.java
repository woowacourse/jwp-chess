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

    @DisplayName("create로 새 게임을 생성한다")
    @Test
    void create() {
        gameDao.create(LOG_IN_DTO);
        assertThat(gameDao.isInId("1234")).isTrue();
    }

    @DisplayName("게임이 끝나지 않으면 foce_end_flag는 false이다")
    @Test
    void findForceEndFlagById() {
        gameDao.create(LOG_IN_DTO);
        assertThat(gameDao.findForceEndFlag("1234")).isFalse();
    }

    @DisplayName("findForceEndFlag에서 존재하지 않는 게임아이디 조회시 예외가 발생한다")
    @Test
    void findForceEndFlagByIdError() {
        gameDao.create(LOG_IN_DTO);
        assertThatThrownBy(() -> gameDao.findForceEndFlag("124"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 제목의 방을 찾을 수 없습니다.");
    }

    @DisplayName("findTurn에서 첫 이동불가능한 turn은 black이다")
    @Test
    void findTurn() {
        gameDao.create(LOG_IN_DTO);
        assertThat(gameDao.findTurn("1234")).isEqualTo(Color.BLACK);
    }

    @DisplayName("updateTurn로 게임을 업데이트한다")
    @Test
    void updateTurnById() {
        gameDao.create(LOG_IN_DTO);
        gameDao.updateTurn(Color.WHITE, "1234");
        assertThat(gameDao.findTurn("1234")).isEqualTo(Color.WHITE);
    }

    @DisplayName("updateForceEndFlag를 통해 force_end_flag를 업데이트한다")
    @Test
    void updateForceEndFlag() {
        gameDao.create(LOG_IN_DTO);
        gameDao.updateForceEndFlag(true, "1234");
        assertThat(gameDao.findForceEndFlag("1234")).isEqualTo(true);
    }

    @DisplayName("delete로 게임을 삭제한다")
    @Test
    void delete() {
        gameDao.create(LOG_IN_DTO);
        assertThat(gameDao.isInId("1234")).isTrue();
        gameDao.delete("1234");
        assertThat(gameDao.isInId("1234")).isFalse();
    }

    @DisplayName("isValidPassword로 유효한 아이디의 패스워드인지 확인한다")
    @Test
    void isValidPassword() {
        gameDao.create(LOG_IN_DTO);
        assertThat(gameDao.isValidPassword(LOG_IN_DTO)).isTrue();
        assertThat(gameDao
                .isValidPassword(new LogInDto("1234", "12345")))
                .isFalse();
    }

    @DisplayName("findAllGame로 모든 게임방 리스트를 반환한다")
    @Test
    void findAllGame() {
        gameDao.create(LOG_IN_DTO);
        gameDao.create(new LogInDto("2", "1234"));
        gameDao.create(new LogInDto("3", "1234"));
        assertThat(gameDao.findAllGame().size()).isEqualTo(3);
    }
}
