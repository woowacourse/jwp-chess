package chess.unittest;

import chess.controller.RoomRestController;
import chess.service.PlayerService;
import chess.service.RoomService;
import chess.util.CookieHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoomRestController.class)
public class RoomEnterTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private RoomService roomService;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private CookieHandler cookieHandler;

    private Long roomId = 1L;
    private String playerId = "player1";

    @DisplayName("유효한 방 입장")
    @Test
    public void testEnterRoom() throws Exception {
        final RequestBuilder request = MockMvcRequestBuilders.post("/room/enter/" + roomId)
                .content(playerId)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print());
    }

    // XXX :: cookieHandler가 mock이라 cookie가 등록되지 않는다.
    // 쿠키의 등록을 어떻게 확인할 수 있을까.
    @DisplayName("유효한 방 입장 :: 입장 시 쿠키 등록")
    @Test
    public void testAddCookieWhenEnterRoom() throws Exception {
        final RequestBuilder request = MockMvcRequestBuilders.post("/room/enter/" + roomId)
                .content(playerId)
                .contentType(MediaType.APPLICATION_JSON);

        final MvcResult mvcResult = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        final String cookieValue = mvcResult.getResponse().getCookie("web_chess_" + roomId).getValue();
        Assertions.assertThat(cookieValue).isEqualTo(playerId);
    }

    @DisplayName("유효하지 않은 방 입장 :: playerId 누락")
    @Test
    public void testEnterRoomWithoutPlayerId() throws Exception {
        final RequestBuilder request = MockMvcRequestBuilders.post("/room/enter/" + roomId)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
