package chess.domain.game.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.game.ChessGame;
import chess.web.dto.ChessGameDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
class ChessGameDaoTest {

    private ChessGameDao chessGameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        chessGameDao = new ChessGameDao(jdbcTemplate);
    }

    @Test
    void addGame() {
        ChessGame chessGame = ChessGame.initChessGame();
        chessGame.setName("new game");

        Long gameId = chessGameDao.addGame(chessGame);

        assertThat(gameId).isNotNull();
        assertThat(gameId).isNotEqualTo(0L);
    }

    @Test
    void findActiveGames() {
        ChessGame chessGame = ChessGame.initChessGame();
        String gameName = "game1";
        chessGame.setName(gameName);
        chessGameDao.addGame(chessGame);

        List<ChessGameDto> activeGames = chessGameDao.findActiveGames();

        assertThat(activeGames).size().isEqualTo(1);
        assertThat(activeGames.get(0).getName()).isEqualTo(gameName);
    }

    @Test
    void updateGameEnd() {
    }

    @Test
    void findGameById() {
        ChessGame chessGame = ChessGame.initChessGame();
        String gameName = "game2";
        chessGame.setName(gameName);
        Long id = chessGameDao.addGame(chessGame);

        ChessGameDto chessGameDto = chessGameDao.findGameById(String.valueOf(id));

        assertThat(chessGameDto.getId()).isEqualTo(String.valueOf(id));
        assertThat(chessGameDto.getName()).isEqualTo(gameName);
    }
}