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

    private static final String GAME_ID = "1234";
    private static final LogInDto LOG_IN_DTO = new LogInDto(GAME_ID, GAME_ID);
    private GameDao gameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        gameDao = new GameDao(jdbcTemplate);
    }

    @DisplayName("create 로 새 게임을 생성한다")
    @Test
    void create() {
        gameDao.create(LOG_IN_DTO);
        assertThat(gameDao.findRoom(GAME_ID).getId()).isEqualTo(GAME_ID);
    }

    @DisplayName("게임이 끝나지 않으면 force_end_flag 는 false 이다")
    @Test
    void findForceEndFlagById() {
        gameDao.create(LOG_IN_DTO);
        assertThat(gameDao.findForceEndFlag(GAME_ID)).isFalse();
    }

    @DisplayName("findForceEndFlag 에서 존재하지 않는 게임아이디 조회시 예외가 발생한다")
    @Test
    void findForceEndFlagByIdError() {
        gameDao.create(LOG_IN_DTO);
        assertThatThrownBy(() -> gameDao.findForceEndFlag("124"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 제목의 방을 찾을 수 없습니다.");
    }

    @DisplayName("findTurn 에서 첫 이동불가능한 turn 은 black 이다")
    @Test
    void findTurn() {
        gameDao.create(LOG_IN_DTO);
        assertThat(gameDao.findTurn(GAME_ID)).isEqualTo(Color.BLACK);
    }

    @DisplayName("updateTurn 로 게임을 업데이트한다")
    @Test
    void updateTurnById() {
        gameDao.create(LOG_IN_DTO);
        gameDao.updateTurn(Color.WHITE, GAME_ID);
        assertThat(gameDao.findTurn(GAME_ID)).isEqualTo(Color.WHITE);
    }

    @DisplayName("updateForceEndFlag 를 통해 force_end_flag 를 업데이트한다")
    @Test
    void updateForceEndFlag() {
        gameDao.create(LOG_IN_DTO);
        gameDao.updateForceEndFlag(true, GAME_ID);
        assertThat(gameDao.findForceEndFlag(GAME_ID)).isEqualTo(true);
    }

    @DisplayName("delete 로 게임을 삭제한다")
    @Test
    void delete() {
        gameDao.create(LOG_IN_DTO);
        assertThat(gameDao.findRoom(GAME_ID).getId())
                .isEqualTo(GAME_ID);
        gameDao.delete(GAME_ID);
        assertThatThrownBy(() -> gameDao.findRoom(GAME_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 제목의 방을 찾을 수 없습니다.");
    }

    @DisplayName("findAllGame 로 모든 게임방 리스트를 반환한다")
    @Test
    void findAllGame() {
        gameDao.create(LOG_IN_DTO);
        gameDao.create(new LogInDto("2", GAME_ID));
        gameDao.create(new LogInDto("3", GAME_ID));
        assertThat(gameDao.findAllGame().size()).isEqualTo(3);
    }
}
