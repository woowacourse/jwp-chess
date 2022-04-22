package web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import chess.Score;
import chess.piece.Color;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import web.dao.ChessGameDao;
import web.dto.GameStatus;
import web.service.ChessGameService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ChessGameControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChessGameDao chessGameDao;

    @Autowired
    private ChessGameService chessGameService;

    private int chessGameId;

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

        chessGameId = chessGameDao.saveChessGame("hoho", GameStatus.READY, Color.WHITE, new Score(new BigDecimal("38")),
                new Score(new BigDecimal("38")));
        chessGameService.prepareNewChessGame(chessGameDao.findById(chessGameId));
    }

    @Test
    @DisplayName("체스 게임 방 접속")
    void chessGame() throws Exception {
        mockMvc.perform(get("/chess-game").param("chess-game-id", String.valueOf(chessGameId)))
            .andDo(print())
            .andExpectAll(
                    status().isOk(),
                    view().name("chess-game"),
                    model().attributeExists("pieces"),
                    model().attributeExists("chessGame")
            );
    }

    @Test
    @DisplayName("정상적인 기물 이동")
    void move() throws Exception {
        mockMvc.perform(post("/chess-game/move")
                .param("chess-game-id", String.valueOf(chessGameId))
                .param("from", "A2")
                .param("to", "A4"))
                .andDo(print())
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/chess-game?chess-game-id=" + chessGameId),
                        flash().attributeCount(0)
                );
    }

    @Test
    @DisplayName("비정상적인 기물 이동")
    void invalidMove() throws Exception {
        mockMvc.perform(post("/chess-game/move")
                .param("chess-game-id", String.valueOf(chessGameId))
                .param("from", "A2")
                .param("to", "A5"))
                .andDo(print())
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/chess-game?chess-game-id=" + chessGameId),
                        flash().attributeExists("hasError"),
                        flash().attributeExists("errorMessage")
                );
    }

}