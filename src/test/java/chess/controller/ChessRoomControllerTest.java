package chess.controller;

import chess.dao.UserDao;
import chess.dto.request.RoomCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChessRoomControllerTest extends SpringBootBaseTest {

    @Autowired
    public UserDao userDao;


    @BeforeEach
    void init() {
        userDao.create("suri", "123456");
    }


    @DisplayName("방 생성 테스트")
    @Test
    public void testRoomCreate() throws Exception {
        RoomCreateRequest roomRequestDto = new RoomCreateRequest("room", "123456", "suri");

        mvc.perform(post("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roomRequestDto))
                .cookie(new Cookie("user", "suri")))
                .andExpect(status().isOk());
    }



    @DisplayName("방 생성 테스트 예외 - 방 이름 2자 이상")
    @Test
    public void testRoomNameMinValidation() throws Exception {
        RoomCreateRequest roomRequestDto = new RoomCreateRequest("r", "123456", "suri");

        mvc.perform(post("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roomRequestDto))
                .cookie(new Cookie("user", "suri")))
                .andExpect(status().isBadRequest());
    }


    @DisplayName("방 생성 테스트 예외 - 방 이름 8자 이하")
    @Test
    public void testRoomNameMaxValidation() throws Exception {
        RoomCreateRequest roomRequestDto = new RoomCreateRequest("roomroomr", "123456", "suri");

        mvc.perform(post("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roomRequestDto))
                .cookie(new Cookie("user", "suri")))
                .andExpect(status().isBadRequest());
    }


    @DisplayName("방 생성 테스트 예외 - 비밀번호 4자 이상")
    @Test
    public void testRoomPasswordMinValidation() throws Exception {
        RoomCreateRequest roomRequestDto = new RoomCreateRequest("room", "1", "suri");

        mvc.perform(post("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roomRequestDto))
                .cookie(new Cookie("user", "suri")))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("방 생성 테스트 예외 - 비밀번호 8자 이상")
    @Test
    public void testRoomPasswordMaxValidation() throws Exception {
        RoomCreateRequest roomRequestDto = new RoomCreateRequest("room", "123456789", "suri");

        mvc.perform(post("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roomRequestDto))
                .cookie(new Cookie("user", "suri")))
                .andExpect(status().isBadRequest());
    }
}
