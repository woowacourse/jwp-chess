package web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import web.spark.dao.ChessGameDao;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class LobbyControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChessGameDao chessGameDao;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
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
    }

    @Test
    void lobby() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("index"),
                        model().attributeExists("chess-games")
                );
    }

    @Test
    void chessGame() throws Exception {
        mockMvc.perform(post("/create-chess-game")
                .param("name", "hoho"))
                .andDo(print())
                .andExpectAll(
                        redirectedUrl("/")
                );

        assertThat(chessGameDao.existByName("hoho")).isTrue();
    }
}
