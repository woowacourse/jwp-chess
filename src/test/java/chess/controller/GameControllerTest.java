package chess.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import chess.entity.GameEntity;
import chess.service.AuthService;
import chess.service.ChessService;
import chess.service.fixture.EventDaoStub;
import chess.service.fixture.GameDaoStub;
import chess.util.CookieUtil;
import java.util.List;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class GameControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        GameDaoStub gameDao = new GameDaoStub();
        EventDaoStub eventDao = new EventDaoStub();
        AuthService authService = new AuthService(gameDao);
        ChessService chessService = new ChessService(gameDao, eventDao);
        GameController gameController = new GameController(chessService, authService);
        mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();
    }

    @Test
    void createGame() throws Exception {
        int expectedNewGameId = findAllTestData().size() + 1;
        Cookie expectedCookie = CookieUtil.generateGameOwnerCookie(expectedNewGameId);

        MockHttpServletRequestBuilder request = post("/game")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"name\": \"방이름\",\"password\": \"비밀번호\"}");

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(cookie().exists("player_validation"))
                .andExpect(cookie().value("player_validation", expectedCookie.getValue()))
                .andExpect(cookie().maxAge("player_validation", expectedCookie.getMaxAge()))
                .andExpect(cookie().path("player_validation", expectedCookie.getPath()))
                .andExpect(header().string("Location", "/game/" + expectedNewGameId))
                .andExpect(content().string(expectedNewGameId + ""));
    }

    private List<GameEntity> findAllTestData() {
        return List.of(
                new GameEntity(1, "진행중인_게임", true),
                new GameEntity(2, "종료된_게임", false),
                new GameEntity(3, "이미_존재하는_게임명", true),
                new GameEntity(4, "참여자가_있는_게임", true),
                new GameEntity(5, "참여자가_없는_게임", true));
    }
}
