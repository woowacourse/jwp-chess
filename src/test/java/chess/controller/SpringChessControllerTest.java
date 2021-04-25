package chess.controller;

import chess.dto.PositionDTO;
import chess.dto.RoomNameDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class SpringChessControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void currentRoomTest() throws Exception{
        mockMvc.perform(get("/currentRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .sessionAttr("roomName", "testRoomName"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("roomName").value("testRoomName"));
    }

    @Test
    void restartTest() throws Exception {
        mockMvc.perform(post("/restart")
                .contentType(MediaType.APPLICATION_JSON)
                .sessionAttr("roomName", "testRoomName"))
                .andExpect(status().isOk());
    }

    @Test
    void succeedMoveTest() throws Exception {
        String content = objectMapper.writeValueAsString(new PositionDTO("a2", "a4"));

        mockMvc.perform(post("/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .sessionAttr("roomName", "testRoomName"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value("SUCCEED"))
                .andExpect(jsonPath("message").value("Succeed"));
    }

    @Test
    void failMoveTest() throws Exception {
        String content = objectMapper.writeValueAsString(new PositionDTO("a1", "a4"));

        mockMvc.perform(post("/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .sessionAttr("roomName", "testRoomName"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void currentBoardTest() throws Exception {
        mockMvc.perform(get("/currentBoard")
                .contentType(MediaType.APPLICATION_JSON)
                .sessionAttr("roomName", "testRoomName"))
                .andExpect(status().isOk());
    }

    @Test
    void currentTurnTest() throws Exception {
        mockMvc.perform(post("/currentTurn")
                .contentType(MediaType.APPLICATION_JSON)
                .sessionAttr("roomName", "testRoomName"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("turn").value("WHITE"));
    }

    @Test
    void scoreTest() throws Exception {
        mockMvc.perform(post("/score")
                .contentType(MediaType.APPLICATION_JSON)
                .sessionAttr("roomName", "testRoomName"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("whiteScore").value(38.0))
                .andExpect(jsonPath("blackScore").value(38.0));
    }

    @Test
    void roomsTest() throws Exception {
        mockMvc.perform(get("/rooms")
            .contentType(MediaType.APPLICATION_JSON)
            .sessionAttr("roomName", "testRoomName"))
            .andExpect(status().isOk());
    }

    @Test
    void succeedCheckRoomNameTest() throws Exception {
        String content = objectMapper.writeValueAsString(new RoomNameDTO("newRoomName"));

        mockMvc.perform(post("/checkRoomName")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value("SUCCEED"))
                .andExpect(jsonPath("message").value("방 생성 성공!"));
    }

    @Test
    void duplicatedCheckRoomNameTest() throws Exception {
        String content = objectMapper.writeValueAsString(new RoomNameDTO("testRoomName"));

        mockMvc.perform(post("/checkRoomName")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("중복된 방 이름입니다."));
    }
}
