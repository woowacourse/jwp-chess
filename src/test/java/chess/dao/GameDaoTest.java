package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import chess.controller.dto.GameDto;
import chess.domain.GameState;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class GameDaoTest {

    private static final long testGameId = 2;
    private static final String name = "name";
    private static final String password = "password";

    @Autowired
    private GameDao gameDao;

    @DisplayName("게임 저장 테스트")
    @Test
    void save() {
        gameDao.save(name, password);
    }

    @DisplayName("게임 조회")
    @Test
    void createAccount() {
        gameDao.save(name, password);

        Optional<Integer> maybeGameState = gameDao.find(name, password);
        Integer actual = maybeGameState.orElseGet(() -> fail("데이터가 없습니다."));

        assertThat(actual).isGreaterThanOrEqualTo(1);
    }

    @DisplayName("게임 조회 테스트")
    @Test
    void load() {
        gameDao.save(name, password);
        final int gameId = gameDao.find(name, password).get();
        Optional<GameState> maybeGameState = gameDao.load(gameId);
        GameState actual = maybeGameState.orElseGet(() -> fail("데이터가 없습니다."));

        assertThat(actual).isEqualTo(GameState.READY);
    }

    @DisplayName("게임 정보 업데이트 테스트")
    @Test
    void update() {
        gameDao.save(name, password);
        final int gameId = gameDao.find(name, password).get();
        gameDao.updateState(gameId, GameState.WHITE_RUNNING);

        Optional<GameState> maybeGameState = gameDao.load(gameId);
        GameState actual = maybeGameState.orElseGet(() -> fail("데이터가 없습니다."));

        assertThat(actual).isEqualTo(GameState.WHITE_RUNNING);
    }

    @DisplayName("게임 삭제 테스트")
    @Test
    void delete() {
        gameDao.delete(testGameId);
    }

    @DisplayName("게임 방 조회 테스트")
    @Test
    void findAll() {
        gameDao.save(name, password);
        gameDao.save("n1", "p1");
        final List<GameDto> games = gameDao.findAll();
        final List<String> expectedNames = games.stream()
                .map(GameDto::getName)
                .collect(Collectors.toList());
        assertThat(expectedNames).isEqualTo(List.of(name, "n1"));
    }

    @DisplayName("게임 방 비밀번호 조회 테스트")
    @Test
    void find_Password() {
        gameDao.save(name, password);
        final int gameId = gameDao.find(name, password).get();
        final String actualPassword = gameDao.findPassword(gameId);
        assertThat(actualPassword).isEqualTo(password);
    }
}
