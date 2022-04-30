package chess.dao;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import chess.domain.game.LogIn;
import chess.domain.piece.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class GameDaoTest {
    private static final String GAME_ID = "1234";
    private static final LogIn LOG_IN_DTO = new LogIn(GAME_ID, GAME_ID);

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

    @DisplayName("findNoPasswordRoom 에서 룸의 비밀번호를 알 수 없다.")
    @Test
    void findNoPasswordRoom() {
        gameDao.create(LOG_IN_DTO);
        assertThatThrownBy(() -> gameDao.findNoPasswordRoom(GAME_ID).validateLogInPassword(LOG_IN_DTO))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("findNoPasswordRoom 에서 첫 이동불가능한 turn 은 black 이다")
    @Test
    void findTurn() {
        gameDao.create(LOG_IN_DTO);
        assertThat(gameDao.findNoPasswordRoom(GAME_ID).getTurn()).isEqualTo(Color.BLACK);
    }

    @DisplayName("updateTurn 로 게임을 업데이트한다")
    @Test
    void updateTurnById() {
        gameDao.create(LOG_IN_DTO);
        gameDao.updateTurn(Color.WHITE, GAME_ID);
        assertThat(gameDao.findNoPasswordRoom(GAME_ID).getTurn()).isEqualTo(Color.WHITE);
    }

    @DisplayName("updateForceEndFlag 를 통해 force_end_flag 를 업데이트한다")
    @Test
    void updateForceEndFlag() {
        //given
        gameDao.create(LOG_IN_DTO);
        assertThat(gameDao.findNoPasswordRoom(GAME_ID).isEnd()).isFalse();

        //when
        gameDao.updateForceEndFlag(true, GAME_ID);

        //then
        assertThat(gameDao.findNoPasswordRoom(GAME_ID).isEnd()).isTrue();
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
        gameDao.create(new LogIn("2", GAME_ID));
        gameDao.create(new LogIn("3", GAME_ID));
        assertThat(gameDao.findAllRoom().size()).isEqualTo(3);
    }

    @DisplayName("findAllGame 에서 게임방이 없을 시 예외처리한다")
    @Test
    void findAllGameError() {
        assertThat(gameDao.findAllRoom().size()).isEqualTo(0);
    }
}
