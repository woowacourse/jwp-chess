package chess.domain.piece.strategy;

import chess.domain.board.ChessBoard;
import chess.domain.exception.InvalidMoveStrategyException;
import chess.domain.piece.Color;
import chess.domain.piece.Knight;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class KnightMoveStrategyTest {
    private ChessBoard chessBoard;

    @DisplayName("나이트의 이동 가능한 경우 테스트")
    @ParameterizedTest
    @CsvSource({
            "d4, e6, NNE",
            "d4, c6, NNW",
            "d4, e2, SSE",
            "d4, c2, SSW",
            "d4, f5, EEN",
            "d4, f3, EEW",
            "d4, b5, WWN",
            "d4, b3, WWS"})
    void knightCanMoveTest(String from, String to, String testCaseDescription) {
        // given
        this.chessBoard = ChessBoard.from(new HashMap<Position, Piece>(){{
            put(Position.of("d4"), new Knight(Color.WHITE));
        }});
        Piece pieceToMove = this.chessBoard.getPieceByPosition(Position.of(from));

        // when
        this.chessBoard.move(Position.of(from), Position.of(to));

        // then
        Piece pieceWhichHasBeenMoved = this.chessBoard.getPieceByPosition(Position.of(to));
        assertThat(pieceWhichHasBeenMoved).isEqualTo(pieceToMove);
    }

    @DisplayName("움직일 수 없는 방향으로 이동하려고 한다면 예외를 던진다.")
    @ParameterizedTest
    @CsvSource({"d4, b4", "d4, e4"})
    void throwExceptionWhenWrongDirection(String from, String to) {
        // given
        this.chessBoard = ChessBoard.from(new HashMap<Position, Piece>(){{
            put(Position.of("d4"), new Knight(Color.WHITE));
        }});

        // when, then
        assertThatThrownBy(() -> chessBoard.move(Position.of(from), Position.of(to)))
                .isInstanceOf(InvalidMoveStrategyException.class)
                .hasMessage("움직일 수 없는 방향입니다.");
    }

    @DisplayName("목적지에 같은 팀의 말이 있다면 예외를 던진다.")
    @Test
    void throwExceptionWhenMoveToSameTeam() {
        // given
        this.chessBoard = ChessBoard.from(new HashMap<Position, Piece>(){{
            put(Position.of("d4"), new Knight(Color.WHITE));
            put(Position.of("e6"), new Knight(Color.WHITE));
        }});

        // when, then
        assertThatThrownBy(() -> chessBoard.move(Position.of("d4"), Position.of("e6")))
                .isInstanceOf(InvalidMoveStrategyException.class)
                .hasMessage("동일한 진영의 말이 있어서 행마할 수 없습니다.");
    }
}