package chess.service;

import chess.domain.piece.Color;
import chess.domain.state.Result;
import chess.dto.ChessGameDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ChessServiceTest {

    private ChessService chessService;

    @BeforeEach
    void setUp() {
        chessService = new ChessService(new FakeChessDao());
    }

    @Test
    void newGame() {
        ChessGameDto chessGameDto = chessService.newGame();
        assertAll(() -> {
            assertThat(chessGameDto.getPositionsAndPieces().keySet().size()).isEqualTo(32);
            assertThat(chessGameDto.getWhiteScore().get(Color.WHITE)).isEqualTo(38);
            assertThat(chessGameDto.getBlackScore().get(Color.BLACK)).isEqualTo(38);
            assertThat(chessGameDto.getResult()).isEqualTo(Result.EMPTY);
        });
    }

    @Test
    void move() {
        chessService.newGame();
        ChessGameDto chessGameDto = chessService.move("A2", "A3");
        assertAll(() -> {
            assertThat(chessGameDto.getPositionsAndPieces().containsKey("A2")).isFalse();
            assertThat(chessGameDto.getPositionsAndPieces().containsKey("A3")).isTrue();
        });
    }

    @Test
    void loadGame() {
        chessService.newGame();
        chessService.move("A2", "A3");
        ChessGameDto chessGameDto = chessService.loadGame();
        assertAll(() -> {
            assertThat(chessGameDto.getPositionsAndPieces().containsKey("A2")).isFalse();
            assertThat(chessGameDto.getPositionsAndPieces().containsKey("A3")).isTrue();
            assertThat(chessGameDto.getWhiteScore().get(Color.WHITE)).isEqualTo(38);
            assertThat(chessGameDto.getBlackScore().get(Color.BLACK)).isEqualTo(38);
            assertThat(chessGameDto.getResult()).isEqualTo(Result.EMPTY);
        });
    }
}
