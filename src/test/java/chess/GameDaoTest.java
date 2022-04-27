package chess;

import chess.entity.GameEntity;
import chess.model.dao.GameDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class GameDaoTest {
    GameDao gameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void initPieceDaoTest() {
        jdbcTemplate.execute("DROP TABLE GAMES IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE games\n" +
                "(\n" +
                "    game_id  int         not null AUTO_INCREMENT,\n" +
                "    name     varchar(64) not null,\n" +
                "    password varchar(64) not null,\n" +
                "    turn     varchar(5)  not null,\n" +
                "    primary key (game_id)\n" +
                ");");
        gameDao = new GameDao(jdbcTemplate);
    }

    @AfterEach
    void cleanDB() {
        gameDao.deleteAll();
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

    //TODO 이거 나중에 예외처리할 때 어떻게 할지 생각해보기
//    @Test
//    @DisplayName("턴이 존재하지 않는 경우 무엇을 반환하는지 확인")
//    void getTurn() {
//        String turn = gameDao.findTurnById(1L);
//
//        assertThat(turn).isEmpty();
//    }

    @Test
    @DisplayName("턴이 update 되는지 확인한다")
    void update() {
        long id = gameDao.initGame("room1", "1234");
        gameDao.update("BLACK", id);
        String turn = gameDao.findTurnById(id);

        assertThat(turn).isEqualToIgnoringCase("black");
    }
}
