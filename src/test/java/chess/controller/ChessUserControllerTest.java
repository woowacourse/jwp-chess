package chess.controller;

import chess.dto.request.UserCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChessUserControllerTest extends SpringBootBaseTest {

    @DisplayName("유저 생성 테스트")
    @Test
    public void testUserCreate() throws Exception {
        UserCreateRequest request = new UserCreateRequest("suri", "123456");

        mvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @DisplayName("유저 생성 테스트 - 유저 이름 2자 이상")
    @Test
    public void testUserNameMinValidation() throws Exception {
        UserCreateRequest request = new UserCreateRequest("", "123456");

        mvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("유저 생성 테스트 - 유저 이름 4자 이하")
    @Test
    public void testUserNameMaxValidation() throws Exception {
        UserCreateRequest request = new UserCreateRequest("surisurisuri", "123456");

        mvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("유저 생성 테스트 - 유저 비밀번호 2자 이상")
    @Test
    public void testUserPwMinValidation() throws Exception {
        UserCreateRequest request = new UserCreateRequest("suri", "");

        mvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("유저 생성 테스트 - 유저 비밀번호 8자 이상")
    @Test
    public void testUserPwMaxValidation() throws Exception {
        UserCreateRequest request = new UserCreateRequest("suri", "12345678910");

        mvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    @DisplayName("유저 생성 테스트 - 중복 이름 예외")
    @Test
    public void testUserDuplicatedName() throws Exception {
        UserCreateRequest request = new UserCreateRequest("suri", "123456");

        mvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        mvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("유저 로그인 테스트")
    @Test
    public void testLogin() throws Exception {
        UserCreateRequest request = new UserCreateRequest("suri", "123456");

        mvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        mvc.perform(post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
