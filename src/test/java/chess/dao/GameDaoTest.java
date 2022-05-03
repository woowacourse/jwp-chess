package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.dao.entity.GameEntity;
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
        GameEntity gameEntity = GameEntity.toSave("game", "password", "salt", GameState.READY);
        Long id = gameDao.save(gameEntity);

        List<GameEntity> games = gameDao.findAll();

        assertThat(games.size()).isEqualTo(1);
        assertThat(games.get(0).getId()).isEqualTo(id);
    }

    @DisplayName("전체 게임 조회 테스트")
    @Test
    void find_All_Game_Id() {
        GameEntity gameEntity = GameEntity.toSave("game", "password", "salt", GameState.READY);
        gameDao.save(gameEntity);

        List<GameEntity> actual = gameDao.findAll();

        assertThat(actual.size()).isEqualTo(1);
    }

    @DisplayName("id에 대한 게임 조회 테스트")
    @Test
    void find_By_Id() {
        GameEntity gameEntity = GameEntity.toSave("game", "password", "salt", GameState.READY);
        Long id = gameDao.save(gameEntity);

        GameEntity actual = gameDao.findById(id);

        assertThat(actual.getName()).isEqualTo("game");
    }

    @DisplayName("게임 정보 업데이트 테스트")
    @Test
    void update() {
        GameEntity gameEntity = GameEntity.toSave("game", "password", "salt", GameState.READY);
        Long id = gameDao.save(gameEntity);
        gameDao.updateState(id, GameState.WHITE_RUNNING);

        GameState actual = gameDao.findById(id).getState();

        assertThat(actual).isEqualTo(GameState.WHITE_RUNNING);
    }

    @DisplayName("게임 삭제 테스트")
    @Test
    void delete() {
        GameEntity gameEntity = GameEntity.toSave("game", "password", "salt", GameState.READY);
        Long id = gameDao.save(gameEntity);
        gameDao.delete(id);

        List<GameEntity> actual = gameDao.findAll();

        assertThat(actual.size()).isEqualTo(0);
    }

    @DisplayName("게임 이름이 중복되는 경우 DuplicateKeyException 발생 테스트")
    @Test
    void duplicated_Name() {
        GameEntity gameEntity = GameEntity.toSave("game", "password", "salt", GameState.READY);
        gameDao.save(gameEntity);

        assertThatThrownBy(() -> gameDao.save(gameEntity))
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessage("이미 존재하는 게임입니다.");
    }
}
