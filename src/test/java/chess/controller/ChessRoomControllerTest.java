package chess.controller;

import dto.RoomCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChessRoomControllerTest extends BaseTest {
    @Test
    public void testRoomCreate() throws Exception {
        RoomCreateRequest roomRequestDto = new RoomCreateRequest("room", "123456", "suri");

        mvc.perform(post("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roomRequestDto))
                .cookie(new Cookie("user", "suri")))
                .andExpect(status().isOk());
    }

    @Test
    public void testRoomNameValidation() throws Exception {
        RoomCreateRequest roomRequestDto = new RoomCreateRequest("room", "1", "suri");

        mvc.perform(post("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roomRequestDto))
                .cookie(new Cookie("user", "suri")))
                .andExpect(status().isBadRequest());
    }
}
