package chess.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class GameControllerTest {

    private static final String GAME_ONE_VALID_BLACK_MOVE = "{\"source\":\"a7\",\"target\":\"a5\"}";

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

    @DisplayName("POST /game 요청에 따른 응답 결과 테스트")
    @Nested
    class CreateGameTest {

        @DisplayName("성공시 201, 생성된 게임id를 토대로 응답 메시지의 Set-Cookie 헤더와 Location 헤더값 설정")
        @Test
        void created_201() throws Exception {
            MockHttpServletRequestBuilder request = post("/game")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content("{\"name\":\"방이름\",\"password\":\"비밀번호!!\"}");

            int expectedNewGameId = GameDaoStub.TOTAL_GAME_COUNT + 1;
            Cookie expectedCookie = CookieUtil.generateGameOwnerCookie(expectedNewGameId);

            mockMvc.perform(request)
                    .andExpect(status().isCreated())
                    .andExpect(cookie().value(CookieUtil.KEY, expectedCookie.getValue()))
                    .andExpect(cookie().maxAge(CookieUtil.KEY, expectedCookie.getMaxAge()))
                    .andExpect(cookie().path(CookieUtil.KEY, expectedCookie.getPath()))
                    .andExpect(header().string("Location", "/game/" + expectedNewGameId))
                    .andExpect(content().string(expectedNewGameId + ""));
        }

        @DisplayName("중복되는 게임명으로 게임을 생성하려는 경우 400 예외")
        @Test
        void fail_400() throws Exception {
            MockHttpServletRequestBuilder request = post("/game")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content("{\"name\":\"이미_존재하는_게임명\",\"password\":\"비밀번호!!\"}");

            mockMvc.perform(request)
                    .andExpect(status().isBadRequest());
        }

        @DisplayName("비밀번호의 길이가 5글자 미만인 경우 400 예외 발생")
        @Test
        void shortPassword_400() throws Exception {
            MockHttpServletRequestBuilder request = post("/game")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content("{\"name\":\"새로운_게임명!\",\"password\":\"3글자\"}");

            mockMvc.perform(request)
                    .andExpect(status().is(400));
        }
    }

    @DisplayName("PUT /game/:id 요청에 따른 응답 결과 테스트")
    @Nested
    class UpdateGameTest {

        @DisplayName("현재 차례의 플레이어의 적절한 수정 요청인 경우 200")
        @Test
        void validCookie_200() throws Exception {
            MockHttpServletRequestBuilder request = put("/game/1")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .cookie(CookieUtil.generateOpponentCookie(1))
                    .content(GAME_ONE_VALID_BLACK_MOVE);

            mockMvc.perform(request)
                    .andExpect(status().isOk());
        }

        @DisplayName("현재 차례가 아닌 플레이어의 쿠키인 경우 403 예외")
        @Test
        void wrongTurnCookie_403() throws Exception {
            MockHttpServletRequestBuilder request = put("/game/1")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .cookie(CookieUtil.generateGameOwnerCookie(1))
                    .content(GAME_ONE_VALID_BLACK_MOVE);

            mockMvc.perform(request)
                    .andExpect(status().is(403));
        }

        @DisplayName("쿠키 값이 없이 체스말 이동 시도하는 경우 401 예외")
        @Test
        void noCookie_401() throws Exception {
            MockHttpServletRequestBuilder request = put("/game/1")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(GAME_ONE_VALID_BLACK_MOVE);

            mockMvc.perform(request)
                    .andExpect(status().is(401));
        }

        @DisplayName("게임과 무관한 쿠키만 있는 경우 401 예외")
        @Test
        void unrelatedCookie_401() throws Exception {
            MockHttpServletRequestBuilder request = put("/game/1")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .cookie(new Cookie("unrelated", "cookie"))
                    .content(GAME_ONE_VALID_BLACK_MOVE);

            mockMvc.perform(request)
                    .andExpect(status().is(401));
        }

        @DisplayName("다른 게임과 관련된 쿠키인 경우 403 예외")
        @Test
        void otherGameCookie_403() throws Exception {
            MockHttpServletRequestBuilder request = put("/game/1")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .cookie(CookieUtil.generateGameOwnerCookie(999999))
                    .content(GAME_ONE_VALID_BLACK_MOVE);

            mockMvc.perform(request)
                    .andExpect(status().is(403));
        }
    }

    @DisplayName("POST /game/:id/auth 요청에 따른 응답 결과 테스트")
    @Nested
    class EnterGameTest {

        @DisplayName("방주인의 비밀번호인 경우 200, Set-Cookie에 백색 진영의 쿠키 설정")
        @Test
        void ownerPassword_200() throws Exception {
            MockHttpServletRequestBuilder request = post("/game/4/auth")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content("{\"name\":\"참여자가_있는_게임\",\"password\":\"encrypted4\"}");

            Cookie expectedCookie = CookieUtil.generateGameOwnerCookie(4);

            mockMvc.perform(request)
                    .andExpect(status().isOk())
                    .andExpect(cookie().value(CookieUtil.KEY, expectedCookie.getValue()))
                    .andExpect(cookie().maxAge(CookieUtil.KEY, expectedCookie.getMaxAge()))
                    .andExpect(cookie().path(CookieUtil.KEY, expectedCookie.getPath()));
        }

        @DisplayName("저장된 상대방 플레이어의 비밀번호인 경우 200, Set-Cookie에 흑색 진영의 쿠키 설정")
        @Test
        void opponentPassword_200() throws Exception {
            MockHttpServletRequestBuilder request = post("/game/4/auth")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content("{\"name\":\"참여자가_있는_게임\",\"password\":\"enemy4\"}");

            Cookie expectedCookie = CookieUtil.generateOpponentCookie(4);

            mockMvc.perform(request)
                    .andExpect(status().isOk())
                    .andExpect(cookie().value(CookieUtil.KEY, expectedCookie.getValue()))
                    .andExpect(cookie().maxAge(CookieUtil.KEY, expectedCookie.getMaxAge()))
                    .andExpect(cookie().path(CookieUtil.KEY, expectedCookie.getPath()));
        }

        @DisplayName("상대방 플레이어로 참여 성공시 200, Set-Cookie에 흑색 진영의 쿠키 설정")
        @Test
        void newOpponentPassword_200() throws Exception {
            MockHttpServletRequestBuilder request = post("/game/5/auth")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content("{\"name\":\"참여자가_없는_게임\",\"password\":\"신규_플레이어\"}");

            Cookie expectedCookie = CookieUtil.generateOpponentCookie(5);

            mockMvc.perform(request)
                    .andExpect(status().isOk())
                    .andExpect(cookie().value(CookieUtil.KEY, expectedCookie.getValue()))
                    .andExpect(cookie().maxAge(CookieUtil.KEY, expectedCookie.getMaxAge()))
                    .andExpect(cookie().path(CookieUtil.KEY, expectedCookie.getPath()));
        }

        @DisplayName("상대방이 있는 게임방인 경우, 잘못된 비밀번호를 입력하면 400 예외 발생")
        @Test
        void wrongPassword_400() throws Exception {
            MockHttpServletRequestBuilder request = post("/game/4/auth")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content("{\"name\":\"참여자가_있는_게임\",\"password\":\"wrong\"}");

            mockMvc.perform(request)
                    .andExpect(status().is(400));
        }

        @DisplayName("비밀번호의 길이가 5글자 미만인 경우 400 예외 발생")
        @Test
        void shortPassword_400() throws Exception {
            MockHttpServletRequestBuilder request = post("/game/4/auth")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content("{\"name\":\"참여자가_있는_게임\",\"password\":\"3글자\"}");

            mockMvc.perform(request)
                    .andExpect(status().is(400));
        }
    }
}
