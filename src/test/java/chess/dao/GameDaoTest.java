package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

import chess.controller.dto.response.GameIdentifiers;
import chess.domain.GameState;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;

@JdbcTest
public class GameDaoTest {

    private static final String NOT_HAVE_DATA = "데이터가 없습니다.";

    @Autowired
    private DataSource dataSource;

    private GameDao gameDao;

    @BeforeEach
    void setUp() {
        gameDao = new GameDao(dataSource);
    }

    @DisplayName("게임 저장 테스트")
    @Test
    void save() {
        Long gameId = gameDao.save("game", "password", "salt", GameState.READY);

        assertThat(gameId).isNotNull();
    }

    @DisplayName("전체 게임 조회 테스트")
    @Test
    void find_All_Game_Id() {
        gameDao.save("game", "password", "salt", GameState.READY);

        List<GameIdentifiers> actual = gameDao.findAllGames();

        assertThat(actual.size()).isEqualTo(1);
    }

    @DisplayName("게임 이름 조회 테스트")
    @Test
    void find_Name() {
        Long gameId = gameDao.save("game", "password", "salt", GameState.READY);

        String actual = gameDao.findName(gameId).orElseGet(() -> fail(NOT_HAVE_DATA));

        assertThat(actual).isEqualTo("game");
    }

    @DisplayName("게임 비밀번호 조회 테스트")
    @Test
    void find_Password() {
        Long gameId = gameDao.save("game", "password", "salt", GameState.READY);

        String actual = gameDao.findPassword(gameId).orElseGet(() -> fail(NOT_HAVE_DATA));

        assertThat(actual).isEqualTo("password");
    }

    @DisplayName("salt 조회 테스트")
    @Test
    void find_Salt() {
        Long gameId = gameDao.save("game", "password", "salt", GameState.READY);

        String actual = gameDao.findSalt(gameId).orElseGet(() -> fail(NOT_HAVE_DATA));

        assertThat(actual).isEqualTo("salt");
    }

    @DisplayName("게임 상태 조회 테스트")
    @Test
    void find_State() {
        Long gameId = gameDao.save("game", "password", "salt", GameState.READY);

        GameState actual = gameDao.findState(gameId).orElseGet(() -> fail(NOT_HAVE_DATA));

        assertThat(actual).isEqualTo(GameState.READY);
    }

    @DisplayName("게임 정보 업데이트 테스트")
    @Test
    void update() {
        Long gameId = gameDao.save("game", "password", "salt", GameState.READY);
        gameDao.updateState(gameId, GameState.WHITE_RUNNING);

        GameState actual = gameDao.findState(gameId).orElseGet(() -> fail(NOT_HAVE_DATA));

        assertThat(actual).isEqualTo(GameState.WHITE_RUNNING);
    }

    @DisplayName("게임 삭제 테스트")
    @Test
    void delete() {
        Long gameId = gameDao.save("game", "password", "salt", GameState.READY);
        gameDao.delete(gameId);

        assertThat(gameDao.findAllGames().size()).isEqualTo(0);
    }

    @DisplayName("게임 이름이 중복되는 경우 DuplicateKeyException 발생 테스트")
    @Test
    void duplicated_Name() {
        gameDao.save("game", "password", "salt", GameState.READY);
        assertThatThrownBy(() -> gameDao.save("game", "password", "salt", GameState.READY))
                .isInstanceOf(DuplicateKeyException.class);
    }
}
