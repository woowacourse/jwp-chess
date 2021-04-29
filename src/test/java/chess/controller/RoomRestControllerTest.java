package chess.controller;

import chess.dto.RoomDto;
import chess.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class RoomRestControllerTest {

    private final MockMvc mockMvc;
    private final RoomService roomService;

    @Autowired
    private ObjectMapper mapper;

    private RoomDto roomDto;

    @Autowired
    public RoomRestControllerTest(MockMvc mockMvc, RoomService roomService) {
        this.mockMvc = mockMvc;
        this.roomService = roomService;
    }

    @BeforeEach
    private void init() {
        roomDto = new RoomDto("validName", "player1", "player2");
    }

    @DisplayName("유효한 이름으로 방 생성")
    @Test
    public void createRoom() throws Exception {
        final RequestBuilder request = MockMvcRequestBuilders.post("/room")
                .content(mapper.writeValueAsString(roomDto))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isOk());
    }

    @DisplayName("유효하지 않은 이름으로 방 생성 (너무 짧은 방 이름)")
    @Test
    public void createRoomWithTooShortName() throws Exception {
        roomDto = new RoomDto("a", "player1", "player2");

        final RequestBuilder request = MockMvcRequestBuilders.post("/room")
                .content(mapper.writeValueAsString(roomDto))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().is4xxClientError());
    }

    @DisplayName("유효하지 않은 이름으로 방 생성 (너무 긴 방 이름)")
    @Test
    public void createRoomWithTooLongName() throws Exception {
        roomDto = new RoomDto("tooLongRoomName", "player1", "player2");

        final RequestBuilder request = MockMvcRequestBuilders.post("/room/create")
                .content(mapper.writeValueAsString(roomDto))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().is4xxClientError());
    }

    @DisplayName("player1이 blank인 방 생성 요청")
    @Test
    public void createRoomWithoutPlayer1() throws Exception {
        roomDto = new RoomDto("tooLongRoomName", "", "player2");

        final RequestBuilder request = MockMvcRequestBuilders.post("/room/create")
                .content(mapper.writeValueAsString(roomDto))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().is4xxClientError());
    }

    @DisplayName("player1이 null인 방 생성 요청")
    @Test
    public void createRoomWithNullPlayer1() throws Exception {
        roomDto = new RoomDto("tooLongRoomName", null, "player2");

        final RequestBuilder request = MockMvcRequestBuilders.post("/room/create")
                .content(mapper.writeValueAsString(roomDto))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().is4xxClientError());
    }

    @DisplayName("중복 방 이름 생성 시 404 응답")
    @Test
    public void duplicatedRoom() throws Exception {
        roomService.create(roomDto.getRoomName(), "player1");

        final RequestBuilder request = MockMvcRequestBuilders.post("/room")
                .content(mapper.writeValueAsString(roomDto))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

    @DisplayName("유효한 방 참가")
    @Test
    public void enterRoom() throws Exception {
        roomDto = new RoomDto("tooLongRoomName", "player1", "player2");

        final RequestBuilder request = MockMvcRequestBuilders.post("/room/enter/{}")
                .param("player2", "player2")
                .content(mapper.writeValueAsString(roomDto))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().is4xxClientError());
    }

    @DisplayName("방 삭제 테스트")
    @Test
    public void deleteRoom() throws Exception {
        final long roomId = roomService.create("newRoom", "test");
        final int preSize = roomService.loadList().size();

        final RequestBuilder request = MockMvcRequestBuilders.delete("/room/" + roomId);

        mockMvc.perform(request)
                .andExpect(status().isOk());

        assertThat(preSize - 1).isEqualTo(roomService.loadList().size());
    }

    @DisplayName("방 삭제 테스트 / 쿠키 정상 삭제 확인")
    @Test
    public void deleteRoomWithCookie() throws Exception {
        final long roomId = roomService.create("newRoom", "test");

        final RequestBuilder request = MockMvcRequestBuilders.delete("/room/" + roomId);
        final MvcResult result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        cookie().doesNotExist("web_chess_" + roomId).match(result);
    }
}