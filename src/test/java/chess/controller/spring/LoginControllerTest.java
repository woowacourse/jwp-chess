package chess.controller.spring;

import chess.controller.spring.vo.SessionVO;
import chess.dto.LoginRequestDTO;
import chess.service.spring.RoomService;
import chess.service.spring.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext
@ActiveProfiles("test")
class LoginControllerTest {

    private int roomId;

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
        roomId = roomService.addRoom("room", "pass1");
    }

    @AfterEach
    void tearDown() {
        userService.deleteAllByRoomId(roomId);
        roomService.deleteById(roomId);
    }

    @DisplayName("로그인을 시도한다.")
    @Test
    void login() throws Exception {
        String requestBody = writeHttpBody(roomId, "abc123");

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("/game/" + roomId));
    }

    private String writeHttpBody(int roomId, String password) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(new LoginRequestDTO(roomId, password));
    }

    @DisplayName("이미 방에 두 명의 플레이어가 존재하는 경우 유저가 추가될 수 없다.")
    @Test
    void cannotLogin() throws Exception {
        userService.addUserIntoRoom(roomId, "pass1");
        String requestBody = writeHttpBody(roomId, "abc123");

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }


    @DisplayName("세션이 없으면 로그아웃을 시도할 수 없다.")
    @Test
    void cannotLogout() throws Exception {
        mockMvc.perform(delete("/logout/" + roomId)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("동일한 방 번호의 세션이 존재해도 비밀번호가 다르면 로그아웃을 시도할 수 없다.")
    @Test
    void cannotLogoutInvalidPassword() throws Exception {

        mockMvc.perform(delete("/logout/" + roomId)
                .sessionAttr("session", new SessionVO(roomId, "invalidPassword"))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }
}
