package chess.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @DisplayName("Http Method - GET / 게임 방 목록 조회")
    @Test
    void getRooms() throws Exception {
        mockMvc.perform(get("/"))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @DisplayName("Http Method - POST /game 게임 생성")
    @Test
    void createGame() throws Exception {
        mockMvc.perform(post("/game")
                .contentType("application/x-www-form-urlencoded")
                .param("title", "방제목11")
                .param("password", "비밀번호22"))
            .andDo(print())
            .andExpect(status().is3xxRedirection());
    }

    @DisplayName("Http Method - DELETE /game 게임 삭제")
    @Test
    void getGameByGameId() throws Exception {

        mockMvc.perform(delete("/game")
                .contentType("application/x-www-form-urlencoded")
                .param("id", "2")
                .param("password", "비밀번호"))
            .andDo(print())
            .andExpect(status().isOk());
    }

}
