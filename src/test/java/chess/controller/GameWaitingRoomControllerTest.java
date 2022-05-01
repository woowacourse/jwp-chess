package chess.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class GameWaitingRoomControllerTest {

    @Autowired
    private GameWaitingRoomController gameWaitingRoomController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @DisplayName("Http Method - GET / 게임 방 목록 조회")
    @Test
    void getRooms() throws Exception {
        mockMvc.perform(get("/"))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @DisplayName("Http Method - DELETE / 게임 삭제")
    @Test
    void getGameByGameId() throws Exception {

        mockMvc.perform(delete("/")
                .contentType("application/x-www-form-urlencoded")
                .param("id", "2")
                .param("password", "비밀번호"))
            .andDo(print())
            .andExpect(status().isOk());
    }

}
