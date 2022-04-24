package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.domain.Score;
import chess.domain.piece.Color;
import chess.dao.ChessGameDao;
import chess.dao.PieceDao;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import chess.dto.ChessGameDto;
import chess.dto.GameStatus;

@JdbcTest
public class ChessGameDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ChessGameDao chessGameDao;
    private PieceDao pieceDao;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS piece");
        jdbcTemplate.execute("DROP TABLE IF EXISTS chess_game");
        jdbcTemplate.execute("CREATE TABLE chess_game\n"
                + "(\n"
                + "    id            INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,\n"
                + "    name          VARCHAR(10) NOT NULL,\n"
                + "    status        VARCHAR(10) NOT NULL,\n"
                + "    current_color CHAR(5)     NOT NULL,\n"
                + "    black_score   VARCHAR(10) NOT NULL,\n"
                + "    white_score   VARCHAR(10) NOT NULL\n"
                + ")");
        jdbcTemplate.execute("CREATE TABLE piece\n"
                + "(\n"
                + "    position      CHAR(2)     NOT NULL,\n"
                + "    chess_game_id INT         NOT NULL,\n"
                + "    color         CHAR(5)     NOT NULL,\n"
                + "    type          VARCHAR(10) NOT NULL,\n"
                + "    PRIMARY KEY (position, chess_game_id),\n"
                + "    FOREIGN KEY (chess_game_id) REFERENCES chess_game (id)\n"
                + ")");

        pieceDao = new PieceDao(jdbcTemplate);
        pieceDao.deleteAll();

        chessGameDao = new ChessGameDao(jdbcTemplate);
        chessGameDao.deleteAll();
        chessGameDao.saveChessGame("Chess Game", GameStatus.RUNNING, Color.WHITE, new Score(new BigDecimal("10.0")),
                new Score(new BigDecimal("15.0")));
    }

    @AfterEach
    void tearDown() {
        pieceDao.deleteAll();
        chessGameDao.deleteAll();
    }

    @Test
    void findById() {
        ChessGameDto chessGameDto = chessGameDao.findById(getChessGameId());

        assertAll(() -> {
            assertThat(chessGameDto.getStatus()).isEqualTo(GameStatus.RUNNING);
            assertThat(chessGameDto.getName()).isEqualTo("Chess Game");
            assertThat(chessGameDto.getCurrentColor()).isEqualTo(Color.WHITE);
            assertThat(chessGameDto.getBlackScore()).isEqualTo(new Score(new BigDecimal("10.0")));
            assertThat(chessGameDto.getWhiteScore()).isEqualTo(new Score(new BigDecimal("15.0")));
        });
    }

    @Test
    void updateChessGame() {
        ChessGameDto chessGameDto = chessGameDao.findById(getChessGameId());
        ChessGameDto newChessGameDto = new ChessGameDto(
                chessGameDto.getId(), chessGameDto.getName(), GameStatus.FINISHED,
                new Score(new BigDecimal("20.0")), new Score(new BigDecimal("12.5")), Color.BLACK);
        chessGameDao.updateChessGame(newChessGameDto);

        ChessGameDto expect = chessGameDao.findById(getChessGameId());

        assertAll(() -> {
            assertThat(expect.getStatus()).isEqualTo(newChessGameDto.getStatus());
            assertThat(expect.getCurrentColor()).isEqualTo(newChessGameDto.getCurrentColor());
            assertThat(expect.getBlackScore()).isEqualTo(newChessGameDto.getBlackScore());
            assertThat(expect.getWhiteScore()).isEqualTo(newChessGameDto.getWhiteScore());
        });
    }

    private int getChessGameId() {
        List<ChessGameDto> chessGameDtos = chessGameDao.findAll();
        return chessGameDtos.get(0).getId();
    }
}
