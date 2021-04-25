package chess.controller.web;

import chess.dto.RoomDto;
import chess.service.SpringChessService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ChessViewControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    SpringChessService chessService;

    @Test
    void createRoom() throws Exception {
        int id = chessService.createRoom("test");

        mockMvc.perform(post("/rooms/create")
                .param("roomName", "test")
        )
                .andDo(print())
                .andExpect(redirectedUrl("/rooms/" + (id + 1)));
    }

    @Test
    void enterRoom() throws Exception {
        int id = chessService.createRoom("test");

        mockMvc.perform(get("/rooms/" + id))
                .andDo(print())
                .andExpect(model().attribute("roomNo", id))
                .andExpect(model().attribute("roomName", "test"))
                .andExpect(view().name("game"));
    }

    @Test
    void showRooms() throws Exception {
        chessService.createRoom("test");
        List<RoomDto> rooms = chessService.getAllSavedRooms();

        mockMvc.perform(get("/rooms"))
                .andDo(print())
                .andExpect(model().attribute("rooms", rooms))
                .andExpect(view().name("repository"));
    }

    @Test
    void loadRoom() throws Exception {
        int id = chessService.createRoom("test");

        mockMvc.perform(get("/rooms/load")
                .param("roomNo", String.valueOf(id)))
                .andDo(print())
                .andExpect(redirectedUrl("/rooms/" + id));
    }
}