package chess.unittest;

import chess.controller.RoomRestController;
import chess.dto.RoomDto;
import chess.service.PlayerService;
import chess.service.RoomService;
import chess.util.CookieHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoomRestController.class)
public class RoomCreateRequestTest {
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

    private String roomName = "room";
    private String player1 = "player1";
    private String player2 = "player2";
    private RoomDto testRoom;

    @BeforeEach
    private void initRoom() {
        testRoom = new RoomDto(roomName, player1, player2);
    }

    @DisplayName("방을 생성한다.")
    @Test
    public void testCreateRoom() throws Exception {
        final Long expectRoomId = 1L;

        Mockito.when(roomService.create(testRoom.getRoomName(), testRoom.getPlayer1()))
                .thenReturn(expectRoomId);

        mockMvc.perform(requestBuilder("/room", testRoom))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(expectRoomId)))
                .andDo(print());
    }

    @DisplayName("유효하지 않은 방 생성 :: 너무 긴 방 제목")
    @Test
    public void testCreateRoomWithTooLongName() throws Exception {
        testRoom = new RoomDto("aaaaaaaaaaaaaa", player1, player2);

        mockMvc.perform(requestBuilder("/room", testRoom))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @DisplayName("유효하지 않은 방 생성 :: 너무 짧은 방 제목")
    @Test
    public void testCreateRoomWithTooShortName() throws Exception {
        testRoom = new RoomDto("a", player1, player2);

        mockMvc.perform(requestBuilder("/room", testRoom))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @DisplayName("유효하지 않은 방 생성 :: player1이 null인 경우")
    @Test
    public void testCreateRoomWithoutPlayer1() throws Exception {
        testRoom = new RoomDto(roomName, null, player2);

        mockMvc.perform(requestBuilder("/room", testRoom))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    private RequestBuilder requestBuilder(final String url, final RoomDto roomDto) throws JsonProcessingException {
        return MockMvcRequestBuilders.post(url)
                .content(mapper.writeValueAsString(roomDto))
                .contentType(MediaType.APPLICATION_JSON);
    }
}
