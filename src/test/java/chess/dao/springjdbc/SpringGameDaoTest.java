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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql("classpath:schema.sql")
class SpringGameDaoTest {

    private SpringGameDao springGameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        springGameDao = new SpringGameDao(jdbcTemplate);
    }

    @Test
    @DisplayName("게임의 상태와 턴을 업데이트 하는지 확인")
    void update() {
        GameFactory.setUpGames(jdbcTemplate, "first");
        springGameDao.update(new ChessGameDto(1L,"EMPTY", "BLACK"));
        ChessGameDto foundGame = springGameDao.findById(1L);
        assertAll(() -> {
            assertThat(foundGame.getTurn()).isEqualTo("BLACK");
            assertThat(foundGame.getStatus()).isEqualTo("EMPTY");
        });
    }

    @Test
    @DisplayName("게임의 id를 통해 게임 조회가 정상적인지 확인")
    void findById() {
        GameFactory.setUpGames(jdbcTemplate,"first");
        ChessGameDto foundGame = springGameDao.findById(1L);
        assertThat(foundGame.getName()).isEqualTo("first");
    }

    @Test
    @DisplayName("게임의 상태만 업데이트하는지 확인")
    void updateStatus() {
        GameFactory.setUpGames(jdbcTemplate,"first");
        String expected = "RESULT";
        springGameDao.updateStatus(new StatusDto(expected), 1L);
        ChessGameDto foundGame = springGameDao.findById(1L);
        assertThat(foundGame.getStatus()).isEqualTo(expected);
    }

    @Test
    @DisplayName("저장된 모든 게임이 조회되는지 확인")
    void findAll() {
        GameFactory.setUpGames(jdbcTemplate, "first", "second");
        GamesDto gamesDto = springGameDao.findAll();
        assertThat(gamesDto.getGames()).hasSize(2);
    }

    @Test
    @DisplayName("이름을 받아 게임을 만든 후 id값을 적절하게 반환하는지 확인")
    void createGame() {
        Long createdGameId = springGameDao.createGame("testGameName", "1a2s3d4f");
        assertThat(createdGameId).isEqualTo(1L);
    }

    @Test
    @DisplayName("게임이 존재하는지 확인")
    void existsById() {
        Long createdGameId = springGameDao.createGame("testGameName", "1a2s3d4f");
        assertThat(springGameDao.existsById(createdGameId)).isTrue();
    }

    @Test
    @DisplayName("게임이 존재하지 않는지 확인")
    void doesNotExistsById() {
        assertThat(springGameDao.existsById(2L)).isFalse();
    }

    @Test
    @DisplayName("게임이 모두 삭제되는지 확인")
    void removeAll() {
        GameFactory.setUpGames(jdbcTemplate, "first", "second");
        springGameDao.removeAll();
        GamesDto gamesDto = springGameDao.findAll();

        assertThat(gamesDto.getGames()).isEmpty();
    }
}
