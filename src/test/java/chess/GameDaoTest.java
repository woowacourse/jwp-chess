package chess;

import chess.entity.GameEntity;
import chess.model.dao.GameDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("classpath:schema.sql")
@JdbcTest
class GameDaoTest {
    private GameDao gameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void initPieceDaoTest() {
        gameDao = new GameDao(jdbcTemplate);
    }

    @Test
    @DisplayName("룸 이름과 비밀번호를 받아 방을 생성한다.")
    void initNewGame() {
        String roomName = "room";
        String password = "1234";
        long gameId = gameDao.initGame(roomName, password);

        assertThat(gameId).isOne();
    }

    @Test
    @DisplayName("저장된 게임 전체를 찾는다.")
    void findAllGames() {
        gameDao.initGame("room1", "1234");
        gameDao.initGame("room2", "1234");
        gameDao.initGame("room3", "1234");

        List<GameEntity> gameEntities = gameDao.findAll();

        assertThat(gameEntities.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("id로 방을 삭제한다.")
    void deleteByGameId() {
        long id = gameDao.initGame("room1", "1234");

        gameDao.deleteByGameId(id);

        assertThat(gameDao.findAll().size()).isZero();
    }

    @Test
    @DisplayName("턴이 초기에 저장되었는지 확인한다")
    void init() {
        long id = gameDao.initGame("room1", "1234");
        String turn = gameDao.findTurnById(id);

        assertThat(turn).isEqualToIgnoringCase("white");
    }

    @Test
    @DisplayName("턴이 update 되는지 확인한다")
    void update() {
        long id = gameDao.initGame("room1", "1234");
        gameDao.update("BLACK", id);
        String turn = gameDao.findTurnById(id);

        assertThat(turn).isEqualToIgnoringCase("black");
    }
}
