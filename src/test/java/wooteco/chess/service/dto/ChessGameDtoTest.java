package wooteco.chess.service.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import wooteco.chess.domain.chessBoard.ChessBoard;
import wooteco.chess.domain.chessBoard.ChessBoardInitializer;
import wooteco.chess.domain.chessGame.ChessGame;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ChessGameDtoTest {

    @ParameterizedTest
    @NullSource
    void of_NullChessGame_ExceptionThrown(final ChessGame chessGame) {
        assertThatThrownBy(() -> ChessGameDto.of(Long.valueOf(1), chessGame))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("체스 게임이 null입니다.");
    }

    @Test
    void of_ChessGame_GenerateInstance() {
        final ChessBoard chessBoard = new ChessBoard(ChessBoardInitializer.create());
        final ChessGame chessGame = ChessGame.from(chessBoard);

        assertThat(ChessGameDto.of(Long.valueOf(1), chessGame)).isInstanceOf(ChessGameDto.class);
    }

}