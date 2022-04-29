package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

import chess.controller.dto.response.GameIdentifiers;
import chess.domain.GameState;
import chess.util.PasswordEncryptor;
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
    private static final Long TEST_GAME_ID = 1L;

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
        String salt = PasswordEncryptor.generateSalt();
        String password = PasswordEncryptor.encrypt("password", salt);

        gameDao.save(TEST_GAME_ID, "game", password, salt);
    }

    @DisplayName("전체 게임 조회 테스트")
    @Test
    void find_All_Game_Id() {
        String salt = PasswordEncryptor.generateSalt();
        String password = PasswordEncryptor.encrypt("password", salt);
        gameDao.save(TEST_GAME_ID, "game", password, salt);

        List<GameIdentifiers> actual = gameDao.findAllGames();

        assertThat(actual.size()).isEqualTo(1);
    }

    @DisplayName("게임 이름 조회 테스트")
    @Test
    void find_Name() {
        String salt = PasswordEncryptor.generateSalt();
        String password = PasswordEncryptor.encrypt("password", salt);
        gameDao.save(TEST_GAME_ID, "game", password, salt);

        String actual = gameDao.findName(TEST_GAME_ID).orElseGet(() -> fail(NOT_HAVE_DATA));

        assertThat(actual).isEqualTo("game");
    }

    @DisplayName("게임 비밀번호 조회 테스트")
    @Test
    void find_Password() {
        String salt = PasswordEncryptor.generateSalt();
        String password = PasswordEncryptor.encrypt("password", salt);
        gameDao.save(TEST_GAME_ID, "game", password, salt);

        String actual = gameDao.findPassword(TEST_GAME_ID).orElseGet(() -> fail(NOT_HAVE_DATA));

        assertThat(actual).isEqualTo(password);
    }

    @DisplayName("salt 조회 테스트")
    @Test
    void find_Salt() {
        String salt = PasswordEncryptor.generateSalt();
        String password = PasswordEncryptor.encrypt("password", salt);
        gameDao.save(TEST_GAME_ID, "game", password, salt);

        String actual = gameDao.findSalt(TEST_GAME_ID).orElseGet(() -> fail(NOT_HAVE_DATA));

        assertThat(actual).isEqualTo(salt);
    }

    @DisplayName("게임 상태 조회 테스트")
    @Test
    void find_State() {
        String salt = PasswordEncryptor.generateSalt();
        String password = PasswordEncryptor.encrypt("password", salt);
        gameDao.save(TEST_GAME_ID, "game", password, salt);

        GameState actual = gameDao.findState(TEST_GAME_ID).orElseGet(() -> fail(NOT_HAVE_DATA));

        assertThat(actual).isEqualTo(GameState.READY);
    }

    @DisplayName("게임 정보 업데이트 테스트")
    @Test
    void update() {
        String salt = PasswordEncryptor.generateSalt();
        String password = PasswordEncryptor.encrypt("password", salt);
        gameDao.save(TEST_GAME_ID, "game", password, salt);
        gameDao.updateState(TEST_GAME_ID, GameState.WHITE_RUNNING);

        GameState actual = gameDao.findState(TEST_GAME_ID).orElseGet(() -> fail(NOT_HAVE_DATA));

        assertThat(actual).isEqualTo(GameState.WHITE_RUNNING);
    }

    @DisplayName("게임 삭제 테스트")
    @Test
    void delete() {
        gameDao.delete(TEST_GAME_ID);
    }

    @DisplayName("게임 이름이 중복되는 경우 DuplicateKeyException 발생 테스트")
    @Test
    void duplicated_Name() {
        gameDao.save(TEST_GAME_ID, "name", "password", "salt");
        assertThatThrownBy(() -> gameDao.save(TEST_GAME_ID, "name", "password", "salt"))
                .isInstanceOf(DuplicateKeyException.class);
    }
}
