package chess.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import chess.dto.CreateRoomDto;
import chess.dto.DeleteRoomDto;
import chess.service.RoomService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RoomApiControllerTest {

    private static final Gson GSON = new Gson();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoomService roomService;

    @Test
    void viewRooms() throws Exception {
        roomService.addRoom(new CreateRoomDto("test1", ""));
        roomService.addRoom(new CreateRoomDto("test2", ""));

        mockMvc.perform(get("/api/viewRooms"))
            .andExpect(status().isOk())
            .andExpect(content().json(GSON.toJson(roomService.getUsedRooms())));
    }

    @Test
    void createRoom() throws Exception {
        String body = GSON.toJson(new CreateRoomDto("test",""));

        mockMvc.perform(post("/api/createRoom")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andExpect(status().isOk())
            .andExpect(content().json(GSON.toJson(roomService.getUsedRooms())));
    }

    @Test
    void deleteRoom() throws Exception {
        Integer roomId = (Integer) roomService.getUsedRooms().getRooms().keySet().toArray()[0];
        String body = GSON.toJson(new DeleteRoomDto(roomId));

        mockMvc.perform(post("/api/deleteRoom")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andExpect(status().isOk())
            .andExpect(content().json(GSON.toJson(roomService.getUsedRooms())));
    }
}