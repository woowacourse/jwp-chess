package chess.service;

import chess.domain.piece.Color;
import chess.domain.state.Result;
import chess.dto.ChessGameDto;
import chess.dto.MoveDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ChessServiceTest {

    private ChessService chessService;
    private final int gameId = 0;

    @BeforeEach
    void setUp() {
        chessService = new ChessService(new FakeChessDao());
    }

    @Test
    void newGame() {
        ChessGameDto chessGameDto = chessService.newGame(gameId);
        assertAll(() -> {
            assertThat(chessGameDto.getPositionsAndPieces().keySet().size()).isEqualTo(32);
            assertThat(chessGameDto.getWhiteScore().get(Color.WHITE)).isEqualTo(38);
            assertThat(chessGameDto.getBlackScore().get(Color.BLACK)).isEqualTo(38);
            assertThat(chessGameDto.getResult()).isEqualTo(Result.EMPTY);
        });
    }

    @Test
    void move() {
        chessService.newGame(gameId);
        ChessGameDto chessGameDto = chessService.move(new MoveDto(Integer.toString(gameId), "A2", "A3"));
        assertAll(() -> {
            assertThat(chessGameDto.getPositionsAndPieces().containsKey("A2")).isFalse();
            assertThat(chessGameDto.getPositionsAndPieces().containsKey("A3")).isTrue();
        });
    }

    @Test
    void loadGame() {
        chessService.newGame(gameId);
        chessService.move(new MoveDto(Integer.toString(gameId), "A2", "A3"));
        ChessGameDto chessGameDto = chessService.loadGame(gameId);
        assertAll(() -> {
            assertThat(chessGameDto.getPositionsAndPieces().containsKey("A2")).isFalse();
            assertThat(chessGameDto.getPositionsAndPieces().containsKey("A3")).isTrue();
            assertThat(chessGameDto.getWhiteScore().get(Color.WHITE)).isEqualTo(38);
            assertThat(chessGameDto.getBlackScore().get(Color.BLACK)).isEqualTo(38);
            assertThat(chessGameDto.getResult()).isEqualTo(Result.EMPTY);
        });
    }
}
