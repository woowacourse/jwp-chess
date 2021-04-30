package chess.controller.chessgame;

import chess.chessgame.domain.room.Room;
import chess.chessgame.domain.room.game.board.piece.attribute.Color;
import chess.service.ChessService;
import chess.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static chess.chessgame.domain.room.game.board.piece.attribute.Color.BLACK;
import static chess.chessgame.domain.room.game.board.piece.attribute.Color.WHITE;
import static chess.chessgame.domain.room.game.board.position.Position.of;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GameRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RoomService roomService;
    @Autowired
    private ChessService chessService;
    @Autowired
    private ObjectMapper objectMapper;

    private Room fixture;

    @BeforeEach
    void setUp() {
        fixture = roomService.createRoom("name", "1234");
    }

    @DisplayName("방의 점수를 잘 받아오는지 확인")
    @Test
    void getScoreTest() throws Exception {
        Map<Color, Double> expectedScores = fixture.getGameManager().getBoard().getScoreMap();
        double whiteScore = expectedScores.get(WHITE);
        double blackScore = expectedScores.get(BLACK);

        mockMvc.perform(
                get("/game/" + fixture.getGameManager().getId() + "/score")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("colorsScore.WHITE").value(whiteScore))
                .andExpect(jsonPath("colorsScore.BLACK").value(blackScore));
    }

    @DisplayName("게임이 잘 로딩되는지 확인")
    @Test
    void loadGameTest() throws Exception {
        chessService.move(fixture.getGameManager().getId(), WHITE, of("a2"), of("a3"));

        mockMvc.perform(
                get("/game/1")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("piecesAndPositions.a3.notation").value("p"))
                .andExpect(jsonPath("piecesAndPositions.a3.color").value("WHITE"));
    }

    @DisplayName("말의 이동이 수행되는지 확인")
    @Test
    void movePiece() throws Exception {
        MoveRequestDto moveRequestDto = new MoveRequestDto(fixture.getGameManager().getId(), fixture.getRoomId(), "b2", "b3");
        MockHttpSession mockHttpSession = new MockHttpSession();

        mockHttpSession.setAttribute("user", fixture.getWhiteUser());

        mockMvc.perform(
                put("/game/move")
                        .session(mockHttpSession)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(moveRequestDto))
        )
                .andDo(print())
                .andExpect(status().isOk());
    }
}