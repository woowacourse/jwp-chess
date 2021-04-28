package chess.controller.spring;

import chess.controller.spring.vo.SessionVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class MoveControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("세션이 없으면 로그인 페이지로 이동한다.")
    @Test
    void moveToLoginPage() throws Exception {
        mockMvc.perform(get("/game/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @DisplayName("이미 게임에 참여중이라면 해당 게임을 제외한 타인의 방에는 접속할 수 없다.")
    @Test
    void cannotAccessToOtherGame() throws Exception {
        mockMvc.perform(get("/game/1")
                .sessionAttr("session", new SessionVO(2, "pass123")))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("로그인(세션) 상태에서 자신의 게임 페이지로 정상 이동된다.")
    @Test
    void moveToMyGamePage() throws Exception {
        mockMvc.perform(get("/game/1")
                .sessionAttr("session", new SessionVO(1, "abc123")))
                .andExpect(status().isOk())
                .andExpect(view().name("game"));
    }
}
