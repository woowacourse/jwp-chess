package chess.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import chess.controller.api.LobbyController;
import chess.domain.game.ChessGame;
import chess.dto.TitleDto;
import chess.service.LobbyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(controllers = {LobbyController.class, ExceptionController.class})
public class LobbyControllerTest {

    @MockBean
    private LobbyService lobbyService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("새 게임 생성, 중복된 이름일 때 bad request")
    void duplicated() throws Exception {
        String content = objectMapper.writeValueAsString(new TitleDto("test-room"));

        when(lobbyService.findGame("test-room")).thenReturn(Optional.of("1"));

        mockMvc.perform(post("/game")
            .content(content)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("새 게임 생성 성공")
    void createGame() throws Exception {
        String content = objectMapper.writeValueAsString(new TitleDto("test-room"));

        when(lobbyService.newGame("test-room")).thenReturn("1");

        mockMvc.perform(post("/game")
            .content(content)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(is("1")));
    }

    @Test
    @DisplayName("생성된 게임 방 목록 불러오기")
    void allGames() throws Exception {
        when(lobbyService.findAllGames()).thenReturn(Arrays.asList(
            new ChessGame(),
            new ChessGame(),
            new ChessGame()
        ));

        mockMvc.perform(get("/games"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("roomList", hasSize(3)));
    }
}
