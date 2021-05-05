package chess.controller.spring;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import chess.controller.spring.util.CookieParser;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ChessControllerTest {
    @Autowired
    private ChessController chessController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void handleGamesWithUserId() throws Exception {
        String userId = "1";
        Cookie userIdCookie = new Cookie("userId", CookieParser.encodeCookie(userId));
        mockMvc.perform(
            get("/games")
                .cookie(userIdCookie)
        ).andExpect(status().is3xxRedirection());
    }

    @Test
    public void handleGamesWithoutUserId() throws Exception {
        String userId = "1";
        Cookie userIdCookie = new Cookie("userId", CookieParser.encodeCookie(userId));
        mockMvc.perform(
            get("/games")
                .cookie(userIdCookie)
        ).andExpect(status().is3xxRedirection());
    }
}