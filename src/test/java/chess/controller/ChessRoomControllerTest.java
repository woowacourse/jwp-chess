package chess.controller;

import dto.RoomRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChessRoomControllerTest extends BaseTest {
    @Test
    public void testRoomCreate() throws Exception {
        RoomRequestDto roomRequestDto = new RoomRequestDto(null, "room", "123456", null,null);

        mvc.perform(post("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roomRequestDto))
                .cookie(new Cookie("user", "suri")))
                .andExpect(status().isOk());
    }

    @Test
    public void testRoomNameValidation() throws Exception {
        RoomRequestDto roomRequestDto = new RoomRequestDto(null, "room", "1", null,null);

        mvc.perform(post("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roomRequestDto))
                .cookie(new Cookie("user", "suri")))
                .andExpect(status().isBadRequest());
    }
}
