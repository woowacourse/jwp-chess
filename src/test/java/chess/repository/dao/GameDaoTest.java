package chess.repository.dao;

import chess.domain.ChessGameManager;
import chess.domain.piece.Color;
import chess.domain.position.Position;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class GameDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private GameDao gameDao;
    private ChessGameManager chessGameManager = new ChessGameManager();

    @BeforeEach
    void setUp() {
        this.gameDao = new GameDao(jdbcTemplate);
        chessGameManager = new ChessGameManager();
        chessGameManager.start();
    }

    @Test
    @DisplayName("게임을 저장하고 고유 값을 얻어온다.")
    void saveTest() {
        // when
        long gameId = gameDao.save(chessGameManager);

        // then
        assertThat(gameId).isInstanceOf(Long.class);
    }

    @Test
    @DisplayName("저장된 게임에서 현재 순서를 불러온다.")
    void findCurrentTurnById() {
        // given
        Color currentTurn = chessGameManager.getCurrentTurnColor();

        // when
        long gameId = gameDao.save(chessGameManager);
        Color currentTurnFound = gameDao.findCurrentTurnByGameId(gameId);

        // then
        assertThat(currentTurn).isEqualTo(currentTurnFound);
    }

    @Test
    @DisplayName("순서를 업데이트한다.")
    void updateTurnByGameIdTest() {
        // given
        long gameId = gameDao.save(chessGameManager);
        chessGameManager.move(Position.of("a2"), Position.of("a4"));
        Color currentTurn = chessGameManager.getCurrentTurnColor();

        // when
        gameDao.updateTurnByGameId(chessGameManager, gameId);
        Color currentTurnFound = gameDao.findCurrentTurnByGameId(gameId);

        // then
        assertThat(currentTurn).isEqualTo(currentTurnFound);
    }

    @Test
    @DisplayName("게임을 삭제한다.")
    void deleteTest() {
        // given
        long gameId = gameDao.save(chessGameManager); // to foreignKey
        Position position = Position.of("a2");

        // when
        gameDao.delete(gameId);

        // then
        Integer rowFound = this.jdbcTemplate.queryForObject("SELECT count(*) FROM game WHERE game_id = " + Long.toString(gameId), Integer.class);
        assertThat(rowFound).isEqualTo(0);
    }

    @AfterEach
    void flush() {
        this.jdbcTemplate.execute("DELETE FROM game");
    }
}