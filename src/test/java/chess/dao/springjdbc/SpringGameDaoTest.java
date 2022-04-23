package chess.dao.springjdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.service.dto.ChessGameDto;
import chess.service.dto.GamesDto;
import chess.service.dto.StatusDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class SpringGameDaoTest {

    private SpringGameDao springGameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        springGameDao = new SpringGameDao(jdbcTemplate);
        jdbcTemplate.execute("DROP TABLE game IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE game\n"
                + "(   id     INT NOT NULL AUTO_INCREMENT UNIQUE,\n"
                + "    name   VARCHAR(20) NOT NULL ,\n"
                + "    status VARCHAR(10) NOT NULL DEFAULT 'empty',\n"
                + "    turn   VARCHAR(10) NOT NULL DEFAULT 'white',\n"
                + "    PRIMARY KEY (id))");
    }

    @Test
    @DisplayName("update : 게임의 상태와 턴을 업데이트 하는지 확인")
    void update() {
        GameFactory.setUpGames(jdbcTemplate, "first");
        springGameDao.update(new ChessGameDto(1,"EMPTY", "BLACK"));
        ChessGameDto foundGame = springGameDao.findById(1);
        assertAll(() -> {
            assertThat(foundGame.getTurn()).isEqualTo("BLACK");
            assertThat(foundGame.getStatus()).isEqualTo("EMPTY");
        });
    }

    @Test
    @DisplayName("findById : 게임의 id를 통해 게임 조회가 정상적인지 확인")
    void findById() {
        GameFactory.setUpGames(jdbcTemplate,"first");
        ChessGameDto foundGame = springGameDao.findById(1);
        assertThat(foundGame.getName()).isEqualTo("first");
    }

    @Test
    @DisplayName("updateStatus: 게임의 상태만 업데이트하는지 확인")
    void updateStatus() {
        GameFactory.setUpGames(jdbcTemplate,"first");
        String expected = "RESULT";
        springGameDao.updateStatus(new StatusDto(expected), 1);
        ChessGameDto foundGame = springGameDao.findById(1);
        assertThat(foundGame.getStatus()).isEqualTo(expected);
    }

    @Test
    @DisplayName("findAll: 저장된 모든 게임이 조회되는지 확인")
    void findAll() {
        GameFactory.setUpGames(jdbcTemplate, "first", "second");
        GamesDto gamesDto = springGameDao.findAll();
        assertThat(gamesDto.getGames()).hasSize(2);
    }

    @Test
    @DisplayName("createGame: 이름을 받아 게임을 만든 후 id값을 적절하게 반환하는지 확인")
    void createGame() {
        int createdGameId = springGameDao.createGame("testGameName");
        assertThat(createdGameId).isEqualTo(1);
    }
}
