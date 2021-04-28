package chess.controller;

import chess.domain.User;
import dto.RoomRequestDto;
import dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChessUserControllerTest extends BaseTest{

    @Test
    public void testUserCreate() throws Exception {
        UserDto userDto = new UserDto("suri", "123456", null);

        mvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUserNameValidation() throws Exception {
        UserDto userDto = new UserDto("", "123456", null);

        mvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUserPwValidation() throws Exception {
        UserDto userDto = new UserDto("suri", "", null);

        mvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLogin() throws Exception {
        UserDto userDto = new UserDto("suri", "123456", null);

        mvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

        mvc.perform(post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());
    }
}
