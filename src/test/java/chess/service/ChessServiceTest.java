package chess.service;

import static org.assertj.core.api.Assertions.*;

import chess.domain.game.GameId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import chess.domain.piece.PieceColor;
import chess.domain.position.Position;
import chess.dto.request.UpdatePiecePositionDto;
import chess.dto.response.BoardDto;
import chess.dto.response.ScoreResultDto;

class ChessServiceTest {
    private final GameId GAME_ID = GameId.from("test-game-id");

    private ChessService chessService;

    @BeforeEach
    void setUp() {
        chessService = new ChessService(new GameDaoFake(), new BoardDaoFake());
        chessService.createGame(GAME_ID);
    }

    @DisplayName("게임 초기화")
    @Test
    void initializeGame() {
        // given
        chessService.initializeGame(GAME_ID);
        BoardDto board = chessService.getBoard(GAME_ID);

        // when
        int actual = board.getValue().keySet().size();

        // then
        assertThat(actual).isEqualTo(32);
    }

    @DisplayName("기물 이동")
    @Test
    void movePiece() {
        chessService.movePiece(GAME_ID, Position.from("a2"), Position.from("a3"));
    }

    @DisplayName("현재 차례 색상 가져오기")
    @Test
    void getCurrentTurn() {
        // given & when
        PieceColor actual = chessService.getCurrentTurn(GAME_ID).getPieceColor();

        // then
        assertThat(actual).isEqualTo(PieceColor.WHITE);
    }

    @DisplayName("검정팀 점수 가져오기")
    @Test
    void getScore_black() {
        // given
        ScoreResultDto scoreResultDto = chessService.getScore(GAME_ID);

        // when
        double actual = scoreResultDto.getBlackScore();

        // then
        assertThat(actual).isEqualTo(38.0);
    }

    @DisplayName("흰팀 점수 가져오기")
    @Test
    void getScore_white() {
        // given
        ScoreResultDto scoreResultDto = chessService.getScore(GAME_ID);

        // when
        double actual = scoreResultDto.getWhiteScore();

        // then
        assertThat(actual).isEqualTo(38.0);
    }

    @DisplayName("승자팀 가져오기")
    @Test
    void getWinColor() {
        // given
        chessService.movePiece(GAME_ID, Position.from("b1"), Position.from("c3"));
        chessService.movePiece(GAME_ID, Position.from("a7"), Position.from("a6"));
        chessService.movePiece(GAME_ID, Position.from("c3"), Position.from("b5"));
        chessService.movePiece(GAME_ID, Position.from("a6"), Position.from("a5"));
        chessService.movePiece(GAME_ID, Position.from("b5"), Position.from("c7"));
        chessService.movePiece(GAME_ID, Position.from("a5"), Position.from("a4"));
        chessService.movePiece(GAME_ID, Position.from("c7"), Position.from("e8"));

        // when
        PieceColor actual = chessService.getWinColor(GAME_ID).getPieceColor();

        // then
        assertThat(actual).isEqualTo(PieceColor.WHITE);
    }
}
