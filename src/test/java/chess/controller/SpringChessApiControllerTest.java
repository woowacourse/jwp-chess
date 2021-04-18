package chess.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import chess.MockChessService;
import chess.dto.web.RoomDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class SpringChessApiControllerTest {

    private final MockChessService mockChessService = new MockChessService();

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(new SpringChessApiController(mockChessService)).build();
    }

    @DisplayName("방 생성")
    @Test
    void createRoom() throws Exception {
        String content = new ObjectMapper()
            .writeValueAsString(new RoomDto("1", "roomName", "white", "black"));

        mockMvc
            .perform(post("/room").content(content).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("success"))
            .andExpect(jsonPath("$.roomId").value("1"))
            .andDo(print());
    }

    @DisplayName("방 안의 사용자들")
    @Test
    void usersInRoom() throws Exception {
        mockMvc.perform(get("/room/1/statistics"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.whiteName").value("white"))
            .andExpect(jsonPath("$.blackName").value("black"))
            .andExpect(jsonPath("$.whiteWin").value("0"))
            .andExpect(jsonPath("$.whiteLose").value("0"))
            .andExpect(jsonPath("$.blackWin").value("0"))
            .andExpect(jsonPath("$.blackLose").value("0"));
    }

    @DisplayName("게임 상태")
    @Test
    void gameStatus() throws Exception {
        mockMvc.perform(get("/room/1/getGameStatus"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.gameState").value("Ready"))
            .andExpect(jsonPath("$.turn").value("w"))
            .andExpect(jsonPath("$.winner").value("n"));
    }

    @DisplayName("게임 시작")
    @Test
    void start() throws Exception {
        mockMvc.perform(put("/room/1/start"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.board").hasJsonPath());
    }

    @DisplayName("게임 종료")
    @Test
    void exitGame() throws Exception {
        mockMvc.perform(put("/room/1/exit"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("success"));
    }

    @DisplayName("방 삭제")
    @Test
    void closeRoom() throws Exception {
        mockMvc.perform(put("/room/1/close"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("success"));
    }

    @DisplayName("이동 가능한 위치")
    @Test
    void movablePoints() throws Exception {
        mockMvc.perform(get("/room/1/movablePoints/a1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].x").value("a"))
            .andExpect(jsonPath("$.[0].y").value("1"));
    }

    @DisplayName("말 이동")
    @Test
    void move() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("source", "a1");
        body.put("destination", "b2");

        String content = new ObjectMapper()
            .writeValueAsString(body);

        mockMvc.perform(
            post("/room/1/move").contentType(MediaType.APPLICATION_JSON_VALUE).content(content))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.board").hasJsonPath());
    }
}
