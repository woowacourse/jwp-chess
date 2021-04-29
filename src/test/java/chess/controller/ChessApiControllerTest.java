package chess.controller;

import chess.domain.piece.King;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.request.MoveRequestDto;
import chess.dto.request.RoomNameRequestDto;
import chess.dto.request.TurnChangeRequestDto;
import chess.service.ChessService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class ChessApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChessService chessService;

    @DisplayName("게임을 시작할 때 방을 생성한다.")
    @Test
    void start() throws Exception {
        RoomNameRequestDto roomNameRequestDto = new RoomNameRequestDto("우테코");

        given(chessService.addRoom("우테코"))
                .willReturn(1L);

        mockMvc.perform(post("/room")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roomNameRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("roomId").value(1L));
    }

    @DisplayName("DB에서 체스 보드를 가져온다.")
    @Test
    void chess() throws Exception {
        Map<Position, Piece> board = new HashMap<>();
        board.put(Position.valueOf("1", "e"), King.from("k", Position.valueOf("1", "e")));
        board.put(Position.valueOf("2", "b"), Pawn.from("p", Position.valueOf("2", "b")));

        given(chessService.chessBoardFromDB(1L))
                .willReturn(board);

        mockMvc.perform(get("/board/" + 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("chessBoard.*", hasSize(2)));
    }

    @DisplayName("기물을 움직인다.")
    @Test
    void move() throws Exception {
        MoveRequestDto moveRequestDto = new MoveRequestDto("a2", "a4", 1L);

        given(chessService.move("a2", "a4", 1L))
                .willReturn(true);

        mockMvc.perform(post("/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(moveRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("movable").value(true));
    }

    @DisplayName("게임이 진행되면 턴이 변경된다.")
    @Test
    void turn() throws Exception {
        TurnChangeRequestDto turnChangeRequestDto =
                new TurnChangeRequestDto("white", "black", 1L);

        mockMvc.perform(post("/turn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(turnChangeRequestDto)))
                .andExpect(status().isOk());

        verify(chessService, times(1))
                .changeTurn("black", 1L);
    }
}
