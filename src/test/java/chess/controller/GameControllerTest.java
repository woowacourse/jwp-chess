package chess.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import chess.config.AuthArgumentResolver;
import chess.config.CookieArgumentResolver;
import chess.exception.ExceptionResolver;
import chess.fixture.AuthServiceStub;
import chess.fixture.ChessServiceStub;
import chess.fixture.GameDaoStub;
import chess.service.AuthService;
import chess.service.ChessService;
import chess.util.CookieUtil;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SuppressWarnings("NonAsciiCharacters")
class GameControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        AuthService authService = new AuthServiceStub();
        ChessService chessService = new ChessServiceStub();
        GameController gameController = new GameController(chessService, authService);
        mockMvc = MockMvcBuilders.standaloneSetup(gameController)
                .setCustomArgumentResolvers(new AuthArgumentResolver(), new CookieArgumentResolver())
                .setControllerAdvice(new ExceptionResolver())
                .build();
    }

    @DisplayName("POST /game - 생성된 게임id를 토대로 응답 메시지의 Set-Cookie 헤더와 Location 헤더값이 설정된다")
    @Test
    void createGame() throws Exception {
        MockHttpServletRequestBuilder request = post("/game")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"name\":\"방이름\",\"password\":\"비밀번호\"}");

        int expectedNewGameId = GameDaoStub.TOTAL_GAME_COUNT + 1;
        Cookie expectedCookie = CookieUtil.generateGameOwnerCookie(expectedNewGameId);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(cookie().exists(CookieUtil.KEY))
                .andExpect(cookie().value(CookieUtil.KEY, expectedCookie.getValue()))
                .andExpect(cookie().maxAge(CookieUtil.KEY, expectedCookie.getMaxAge()))
                .andExpect(cookie().path(CookieUtil.KEY, expectedCookie.getPath()))
                .andExpect(header().string("Location", "/game/" + expectedNewGameId))
                .andExpect(content().string(expectedNewGameId + ""));
    }
}
