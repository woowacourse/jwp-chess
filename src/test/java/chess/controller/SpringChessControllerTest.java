package chess.controller;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.piece.*;
import chess.domain.team.PiecePositions;
import chess.domain.team.Team;
import chess.service.SpringChessService;
import chess.webdto.view.ChessGameDto;
import chess.webdto.view.MoveRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SpringChessController.class)
class SpringChessControllerTest {
    @MockBean
    private SpringChessService springChessService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("게임 초기화")
    void startNewGame() throws Exception {
        when(springChessService.startNewGame())
                .thenReturn(new ChessGameDto(new ChessGame(Team.blackTeam(), Team.whiteTeam())));

        mockMvc.perform(post("/game"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("currentTurnTeam").value("white"))
                .andExpect(jsonPath("piecePositionByTeam.white.*", hasSize(16)))
                .andExpect(jsonPath("piecePositionByTeam.black.*", hasSize(16)))
                .andExpect(jsonPath("isPlaying").value(true))
                .andExpect(jsonPath("teamScore.black").value(38.0))
                .andExpect(jsonPath("teamScore.white").value(38.0));
    }


    @Test
    @DisplayName("기존 게임 불러오기")
    void loadPrevGame() throws Exception {
        Map<Position, Piece> whiteInfos = new HashMap<>();
        whiteInfos.put(Position.of("d1"), new Queen());
        whiteInfos.put(Position.of("e1"), new King());
        whiteInfos.put(Position.of("b3"), new Rook());

        Map<Position, Piece> blackInfos = new HashMap<>();
        blackInfos.put(Position.of("d8"), new King());
        blackInfos.put(Position.of("e7"), new Pawn(-1));

        Team whiteTeam = new Team(new PiecePositions(whiteInfos));

        ChessGame chessGame = new ChessGame(new Team(new PiecePositions(blackInfos)), whiteTeam, whiteTeam, true);

        when(springChessService.loadPreviousGame())
                .thenReturn(new ChessGameDto(chessGame));

        mockMvc.perform(get("/game/previous"))
                .andDo(print())
                .andExpect(jsonPath("currentTurnTeam").value("white"))
                .andExpect(jsonPath("piecePositionByTeam.white.*", hasSize(3)))
                .andExpect(jsonPath("piecePositionByTeam.white.d1").value("Queen"))
                .andExpect(jsonPath("piecePositionByTeam.black.*", hasSize(2)))
                .andExpect(jsonPath("isPlaying").value(true))
                .andExpect(jsonPath("teamScore.black").value(1.0))
                .andExpect(jsonPath("teamScore.white").value(14.0));
    }

    @Test
    @DisplayName("말 움직이기 - a2 -> a4 이동")
    void move() throws Exception {
        ChessGame chessGame = new ChessGame(Team.blackTeam(), Team.whiteTeam());
        chessGame.move(Position.of("a2"), Position.of("a4"));

        MoveRequestDto moveRequestDto = new MoveRequestDto();
        moveRequestDto.setStart("a2");
        moveRequestDto.setDestination("a4");
        String requestBody = objectMapper.writeValueAsString(moveRequestDto);

        when(springChessService.move(BDDMockito.any(MoveRequestDto.class)))
                .thenReturn(new ChessGameDto(chessGame));

        mockMvc.perform(post("/game/move")
                .content(requestBody)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("currentTurnTeam").value("black"))
                .andExpect(jsonPath("piecePositionByTeam.white.*", hasSize(16)))
                .andExpect(jsonPath("piecePositionByTeam.white", hasKey("a4")))
                .andExpect(jsonPath("piecePositionByTeam.black.*", hasSize(16)))
                .andExpect(jsonPath("isPlaying").value(true))
                .andExpect(jsonPath("teamScore.black").value(38.0));
    }

}