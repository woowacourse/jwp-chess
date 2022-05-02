package chess.web.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import chess.web.dto.CreateRoomRequestDto;
import chess.web.dto.CreateRoomResultDto;
import chess.web.dto.DeleteDto;
import chess.web.dto.DeleteResultDto;
import chess.web.dto.MoveDto;
import chess.web.dto.MoveResultDto;
import chess.web.dto.PlayResultDto;
import chess.web.dto.ReadRoomResultDto;
import chess.web.dto.RoomDto;
import chess.web.service.ChessGameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ChessGameRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChessGameService chessGameService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void create() throws Exception {
        CreateRoomRequestDto createRoomRequestDto = new CreateRoomRequestDto("testTitle", "testPassword");
        String parsedCreateRoomRequestDto = objectMapper.writeValueAsString(createRoomRequestDto);
        CreateRoomResultDto createRoomResultDto = new CreateRoomResultDto(1);
        given(chessGameService.createRoom(createRoomRequestDto)).willReturn(createRoomResultDto);

        mockMvc.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(parsedCreateRoomRequestDto))
                .andExpect(status().isOk());
    }

    @Test
    void start() throws Exception {
        RoomDto roomDto = new RoomDto(1, "testTitle", false, false);

        given(chessGameService.start(1)).willReturn(roomDto);

        mockMvc.perform(post("/rooms/1/start"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("testTitle")));
    }


    @Test
    void play() throws Exception {
        PlayResultDto playResultDto = new PlayResultDto(new HashMap<>(), "WHITE");
        given(chessGameService.play(1)).willReturn(playResultDto);

        mockMvc.perform(post("/rooms/1/play"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("WHITE")));
    }

    @Test
    void readRooms() throws Exception {
        ReadRoomResultDto readRoomResultDto = new ReadRoomResultDto(new ArrayList<>());
        given(chessGameService.findAllRooms()).willReturn(readRoomResultDto);

        mockMvc.perform(get("/rooms"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));
    }

    @Test
    void move() throws Exception {
        MoveDto moveDto = new MoveDto("A2", "A4");
        MoveResultDto moveResultDto = new MoveResultDto(new HashMap<>());
        given(chessGameService.move(moveDto, 1)).willReturn(moveResultDto);

        String parsedMoveDto = objectMapper.writeValueAsString(moveDto);

        mockMvc.perform(patch("/rooms/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(parsedMoveDto))
                .andExpect(status().isOk());
    }

    @Test
    void deleteRoom() throws Exception {
        DeleteDto deleteDto = new DeleteDto("testpassword");
        DeleteResultDto deleteResultDto = new DeleteResultDto(1, true);
        given(chessGameService.delete( 1, deleteDto)).willReturn(deleteResultDto);

        String parsedDeleteDto = objectMapper.writeValueAsString(deleteDto);

        mockMvc.perform(delete("/rooms/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(parsedDeleteDto))
                .andExpect(status().isOk());
    }
}
