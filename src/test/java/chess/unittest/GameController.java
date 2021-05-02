package chess.unittest;

import chess.controller.GameRestController;
import chess.domain.board.position.Position;
import chess.domain.piece.Owner;
import chess.dto.ReachablePositionsDto;
import chess.service.GameService;
import chess.service.PlayerService;
import chess.util.CookieHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameRestController.class)
public class GameController {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    GameService gameService;

    @MockBean
    CookieHandler cookieHandler;

    @MockBean
    PlayerService playerService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private long testRoomId;
    private String roomName = "newRoom";
    private String player1 = "player1";
    private String player2 = "player2";


    @DisplayName("cookie의 사용자가 현재 순서일 경우 reachable 정상 조회")
    @Test
    public void reachableMvc() throws Exception {
        final Position source = new Position("a2");
        final List<String> expectReachable = Arrays.asList("a3", "a4");

        Mockito.when(cookieHandler.search(Mockito.anyLong(), Mockito.any(HttpServletRequest.class)))
                .thenReturn(new Cookie("player1", player1));

        Mockito.when(playerService.ownerOfPlayer(testRoomId, player1))
                .thenReturn(Owner.WHITE);

        Mockito.when(gameService.reachable(testRoomId, source, Owner.WHITE))
                .thenReturn(expectReachable);

        final MvcResult mvcResult = performReachablePositions(source, player1);
        assertThat(mvcResult.getResponse().getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(new ReachablePositionsDto(expectReachable)));
    }

    @DisplayName("cookie의 사용자가 현재 순서가 아닌 경우, reachable 조회할 수 없음")
    @Test
    public void reachableMvcWithoutCookie() throws Exception {
        final Position source = new Position("a2");

        final Owner turnPlayer = Owner.WHITE;

        Mockito.when(cookieHandler.search(Mockito.anyLong(), Mockito.any(HttpServletRequest.class)))
                .thenReturn(new Cookie("player2", player2));

        Mockito.when(playerService.ownerOfPlayer(testRoomId, player2))
                .thenReturn(Owner.BLACK);

        Mockito.when(gameService.reachable(testRoomId, source, turnPlayer.reverse()))
                .thenReturn(Collections.EMPTY_LIST);

        final MvcResult mvcResult = performReachablePositions(source, player2);
        assertThat(mvcResult.getResponse().getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(new ReachablePositionsDto(Collections.EMPTY_LIST)));
    }

    private MvcResult performReachablePositions(Position source, String userName) throws Exception {
        final RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/game/reachable/" + testRoomId)
                .param("source", source.parseAsString())
                .cookie(new Cookie("web_chess_" + testRoomId, userName));
        return mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
    }
}
