package chess.controller;

import chess.domain.board.position.Position;
import chess.domain.piece.Owner;
import chess.dto.ReachablePositionsDto;
import chess.service.GameService;
import chess.service.PlayerService;
import chess.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class GameRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoomService roomService;

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private ObjectMapper objectMapper;

    private long testRoomId;
    private String roomName = "newRoom";
    private String player1 = "player1";
    private String player2 = "player2";

    @BeforeEach
    private void initTestRoom() {
        testRoomId = roomService.create(roomName, player1);
    }

    @DisplayName("cookie의 사용자가 현재 순서일 경우 reachable 정상 조회")
    @Test
    public void reachableMvc() throws Exception {
        final Position source = new Position("a2");
        final Owner ownerOfTurn = Owner.WHITE;

        final GameService mockService = Mockito.mock(GameService.class);

        final List<String> expectReachable = Arrays.asList("a3", "a4");
        Mockito.when(mockService.reachable(testRoomId, source, ownerOfTurn))
                .thenReturn(expectReachable);

        final MvcResult mvcResult = performReachablePositions(source, player1);
        assertThat(mvcResult.getResponse().getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(new ReachablePositionsDto(expectReachable)));
    }

    @DisplayName("cookie의 사용자가 현재 순서가 아닌 경우, reachable 조회할 수 없음")
    @Test
    public void reachableMvcWithoutCookie() throws Exception {
        final Position source = new Position("a2");
        playerService.enter(testRoomId, player2);

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