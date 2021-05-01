package chess.unittest;

import chess.controller.RoomRestController;
import chess.service.PlayerService;
import chess.service.RoomService;
import chess.util.CookieHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

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
                .content("player1")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print());
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

    @DisplayName("유효하지 않은 방 입장 :: 존재하지 않는 방 Id")
    @Test
    public void testEnterRoomWithInvalidRoomId() throws Exception {

        final RequestBuilder request = MockMvcRequestBuilders.post("/room/enter/" + roomId)
                .content("player1")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
