package chess.controller;

import chess.dao.UserDao;
import dto.RoomCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChessRoomControllerTest extends BaseTest {

    @Autowired
    public UserDao userDao;


    @BeforeEach
    void init() {
        userDao.create("suri", "123456");
    }


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
