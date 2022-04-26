package chess;

import chess.model.dao.GameDao;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

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
        jdbcTemplate.execute("CREATE TABLE GAMES\n" +
                "(\n" +
                "    game_id  int        not null AUTO_INCREMENT,\n" +
                "    password varchar(25) not null,\n" +
                "    turn     varchar(5) not null,\n" +
                "    primary key (game_id)\n" +
                ");");
        gameDao = new GameDao(jdbcTemplate); // @JdbcTest는 일반적인 @ConfigurationProperties와 @Component 빈들은 스캔되지 않는다.
    }

//    @AfterEach
//    void cleanDB() {
//        gameDao.deleteAll();
//    }

    @Test
    void saveGame() {
        Long gameId = gameDao.saveGame("pw1234");
        List<Long> games = gameDao.findAllGameId();

        assertThat(games).containsExactly(gameId);
    }

    @Test
    void findTurnByGameId() {
        Long gameId = gameDao.saveGame("pw1234");
        Optional<String> turn = gameDao.findTurnByGameId(gameId);

        assertThat(turn.get()).isEqualTo("start");
    }

//    @Test
//    @DisplayName("턴이 존재하지 않는 경우 무엇을 반환하는지 확인")
//    void getTurn() {
//        Optional<String> turn = gameDao.findOne();
//
//        assertThat(turn).isEmpty();
//    }

//    @Test
//    @DisplayName("턴이 update 되는지 확인한다")
//    void update() {
//        gameDao.init();
//
//        gameDao.update("BLACK");
//        Optional<String> turn = gameDao.findOne();
//
//        assertThat(turn.get()).isEqualToIgnoringCase("black");
//    }
//
//    @Test
//    @DisplayName("저장된 턴을 모두 삭제한다.")
//    void deleteAll() {
//        gameDao.init();
//        gameDao.deleteAll();
//
//        assertThat(gameDao.findOne()).isEmpty();
//    }
}
