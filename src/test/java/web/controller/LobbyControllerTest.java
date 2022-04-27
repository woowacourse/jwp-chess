package web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import web.dao.ChessGameDao;
import web.dao.RoomDao;
import web.dto.RoomDto;
import web.service.ChessGameService;
import web.service.RoomService;

@SpringBootTest
@AutoConfigureMockMvc
public class LobbyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RoomDao roomDao;

    @Autowired
    private ChessGameDao chessGameDao;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ChessGameService chessGameService;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS piece");
        jdbcTemplate.execute("DROP TABLE IF EXISTS room");
        jdbcTemplate.execute("DROP TABLE IF EXISTS chess_game");
        jdbcTemplate.execute("CREATE TABLE chess_game\n"
                + "(\n"
                + "    id            INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,\n"
                + "    status        VARCHAR(10) NOT NULL,\n"
                + "    current_color CHAR(5)     NOT NULL,\n"
                + "    black_score   VARCHAR(10) NOT NULL,\n"
                + "    white_score   VARCHAR(10) NOT NULL\n"
                + ")");
        jdbcTemplate.execute("CREATE TABLE room\n"
                + "(\n"
                + "    id            INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,\n"
                + "    chess_game_id INT         NOT NULL,\n"
                + "    name          VARCHAR(10) NOT NULL,\n"
                + "    password      VARCHAR(20) NOT NULL,\n"
                + "    FOREIGN KEY (chess_game_id) REFERENCES chess_game (id)\n"
                + ");");
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
        RoomDto room = roomService.saveRoom("verus", "1234");

        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("index"),
                        model().attribute("chess-games", List.of(room))
                );

    }

    @Test
    void createChessGame() throws Exception {
        mockMvc.perform(post("/create-chess-game")
                .param("name", "hoho")
                .param("password", "password"))
                .andDo(print())
                .andExpectAll(
                        status().is3xxRedirection(),
                        new ResultMatcher() {
                            @Override
                            public void match(MvcResult result) throws Exception {
                                String redirectUrl = result.getResponse().getRedirectedUrl();
                                assertThat(redirectUrl).matches("/chess-game\\?chess-game-id=[0-9]*");
                            }
                        }
                );
        RoomDto roomDto = roomDao.findByName("hoho");
        assertThat(roomDto).isNotNull();
    }

    @Test
    void removeChessGame() throws Exception {
        RoomDto room = roomService.saveRoom("verus", "1234");
        mockMvc.perform(post("/remove-chess-game")
                .param("id", String.valueOf(room.getId()))
                .param("password", "1234"))
                .andDo(print())
                .andExpectAll(
                        redirectedUrl("/")
                );
        assertThat(roomDao.findByName("verus")).isNull();
    }

    @Test
    void removeChessGameWithInvalidID() throws Exception {
        roomService.saveRoom("verus", "1234");
        mockMvc.perform(post("/remove-chess-game")
                .param("id", "1234")
                .param("password", "1234"))
                .andDo(print())
                .andExpectAll(
                        redirectedUrl("/"),
                        flash().attributeExists("hasError", "errorMessage")
                );
        assertThat(roomDao.findByName("verus")).isNotNull();
    }

    @Test
    void removeChessGameWithInvalidPassword() throws Exception {
        RoomDto room = roomService.saveRoom("verus", "1234");
        mockMvc.perform(post("/remove-chess-game")
                .param("id", String.valueOf(room.getId()))
                .param("password", "invalidPassword"))
                .andDo(print())
                .andExpectAll(
                        redirectedUrl("/"),
                        flash().attributeExists("hasError", "errorMessage")
                );
        assertThat(roomDao.findByName("verus")).isNotNull();
    }

    @Test
    void removeChessGameIsRunning() throws Exception {
        RoomDto room = roomService.saveRoom("verus", "1234");
        chessGameService.prepareNewChessGame(chessGameDao.findById(room.getChessGameId()));
        chessGameService.move(room.getChessGameId(), new Movement("A2", "A4"));

        mockMvc.perform(post("/remove-chess-game")
                .param("id", String.valueOf(room.getId()))
                .param("password", "1234"))
                .andDo(print())
                .andExpectAll(
                        redirectedUrl("/"),
                        flash().attributeExists("hasError", "errorMessage")
                );

        assertThat(roomDao.findByName("verus")).isNotNull();
    }
}
