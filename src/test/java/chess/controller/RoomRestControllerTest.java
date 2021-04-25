package chess.controller;

import chess.controller.dto.RoomDto;
import chess.service.RoomService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class RoomRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ObjectMapper mapper;

    private RoomDto roomDto;

    @BeforeEach
    private void init() {
        roomDto = new RoomDto();
        roomDto.setRoomName("validName");
        roomDto.setPlayer1("player1");
    }

    @DisplayName("유효한 이름으로 방 생성")
    @Test
    public void createRoom() throws Exception {
        final RequestBuilder request = MockMvcRequestBuilders.post("/room/create")
                .content(mapper.writeValueAsString(roomDto))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isOk());
    }

    @DisplayName("유효하지 않은 이름으로 방 생성 (너무 짧은 방 이름)")
    @Test
    public void createRoomWithTooShortName() throws Exception {
        roomDto.setRoomName("a");

        final RequestBuilder request = MockMvcRequestBuilders.post("/room/create")
                .content(mapper.writeValueAsString(roomDto))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().is4xxClientError());
    }

    @DisplayName("유효하지 않은 이름으로 방 생성 (너무 긴 방 이름)")
    @Test
    public void createRoomWithTooLongName() throws Exception {
        roomDto.setRoomName("tooLongRoomName");

        final RequestBuilder request = MockMvcRequestBuilders.post("/room/create")
                .content(mapper.writeValueAsString(roomDto))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().is4xxClientError());
    }

    @DisplayName("player1이 blank인 방 생성 요청")
    @Test
    public void createRoomWithoutPlayer1() throws Exception {
        roomDto.setPlayer1("");

        final RequestBuilder request = MockMvcRequestBuilders.post("/room/create")
                .content(mapper.writeValueAsString(roomDto))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().is4xxClientError());
    }

    @DisplayName("player1이 null인 방 생성 요청")
    @Test
    public void createRoomWithNullPlayer1() throws Exception {
        roomDto.setPlayer1(null);

        final RequestBuilder request = MockMvcRequestBuilders.post("/room/create")
                .content(mapper.writeValueAsString(roomDto))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().is4xxClientError());
    }

    @DisplayName("중복 방 이름 생성 시 404 응답")
    @Test
    public void duplicatedRoom() throws Exception {
        final RequestBuilder request = MockMvcRequestBuilders.post("/room/create")
                .content(mapper.writeValueAsString(roomDto))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isOk());
        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

    @DisplayName("유효한 방 참가")
    @Test
    public void enterRoom() throws Exception {
        roomDto.setPlayer1("player2");

        final RequestBuilder request = MockMvcRequestBuilders.post("/room/enter/{}")
                .param("player2", "player2")
                .content(mapper.writeValueAsString(roomDto))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().is4xxClientError());
    }

    @DisplayName("방 삭제 테스트")
    @Test
    public void deleteRoom() throws Exception {
        final long roomId = roomService.save("newRoom", "test");
        final int preSize = roomService.loadList().size();

        final RequestBuilder request = MockMvcRequestBuilders.delete("/room/" + roomId);
        mockMvc.perform(request).andExpect(status().isOk());

        assertThat(preSize - 1).isEqualTo(roomService.loadList().size());
    }
}