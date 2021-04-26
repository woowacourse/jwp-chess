package chess.controller;

import chess.domain.board.position.Position;
import chess.domain.piece.Owner;
import chess.service.GameService;
import chess.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class GameRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoomService roomService;

    @Autowired
    private GameService gameService;

    private long testRoomId;
    private String roomName = "newRoom";
    private String player1 = "test";

    @BeforeEach
    private void initTestRoom() {
        testRoomId = roomService.create(roomName, player1);
    }

    @DisplayName("갈 수 있는 위치 리스트 반환 테스트")
    @Test
    public void reachable() {
        final List<String> reachable = gameService.reachable(testRoomId, new Position("a2"), Owner.WHITE);
        assertThat(reachable).contains("a3").contains("a4");
    }

    @DisplayName("갈 수 있는 위치 요청 매핑 확인")
    @Test
    public void reachableMvc() throws Exception {
        final RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/game/reachable/" + testRoomId)
                .param("source", "a2")
                .cookie(new Cookie("web_chess_" + testRoomId, "test"));

        final MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString())
                .isEqualTo("{\"positions\":[\"a3\",\"a4\"]}"); // {"positions":["a3","a4"]}
    }

    @DisplayName("cookie가 일치하지 않는 경우, reachable 조회할 수 없음 확인")
    @Test
    public void reachableMvcWithoutCookie() throws Exception {
        final RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/game/reachable/" + testRoomId)
                .param("source", "a2")
                .cookie(new Cookie("web_chess_" + testRoomId, "test"));

        final MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString())
                .isEqualTo("{\"positions\":[\"a3\",\"a4\"]}"); // {"positions":["a3","a4"]}
    }
}