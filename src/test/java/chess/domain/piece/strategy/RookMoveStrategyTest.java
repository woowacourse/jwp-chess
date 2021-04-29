package chess.domain.piece.strategy;

import chess.domain.board.ChessBoard;
import chess.domain.exception.InvalidMoveStrategyException;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.Rook;
import chess.domain.position.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RookMoveStrategyTest {
    private ChessBoard chessBoard;

    @ParameterizedTest
    @CsvSource({
            "d4, d6, NORTH",
            "d4, d2, SOUTH",
            "d4, f4, EAST",
            "d4, b4, WEST"})
    void rookCanMoveTest(String from, String to, String testCaseDescription) {
        // given
        this.chessBoard = ChessBoard.from(new HashMap<Position, Piece>(){{
            put(Position.of("d4"), new Rook(Color.WHITE));
        }});

        Piece pieceToMove = this.chessBoard.getPieceByPosition(Position.of(from));

        // when
        this.chessBoard.move(Position.of(from), Position.of(to));

        // then
        Piece pieceWhichHasBeenMoved = this.chessBoard.getPieceByPosition(Position.of(to));
        assertThat(pieceWhichHasBeenMoved).isEqualTo(pieceToMove);
    }

    @DisplayName("대각선 방향으로 이동하려고 한다면 예외를 발생시킨다.")
    @ParameterizedTest
    @CsvSource({
            "d4, c3, SOUTHWEST",
            "d4, e3, SOUTHEAST",
            "d4, c5, NORTHWEST",
            "d4, e5, NORTHEAST"})
    void throwExceptionWhenWrongDirection(String from, String to, String testCaseDescription) {
        // given
        this.chessBoard = ChessBoard.from(new HashMap<Position, Piece>(){{
            put(Position.of("d4"), new Rook(Color.WHITE));
        }});

        // when, then
        assertThatThrownBy(() -> chessBoard.move(Position.of(from), Position.of(to)))
                .isInstanceOf(InvalidMoveStrategyException.class)
                .hasMessage("움직일 수 없는 방향입니다.");
    }

    @DisplayName("중간에 다른 기물이 있으면 예외를 발생시킨다.")
    @Test
    void whenBlockedThrowTest() {
        // given
        this.chessBoard = ChessBoard.from(new HashMap<Position, Piece>(){{
            put(Position.of("d4"), new Rook(Color.WHITE));
            put(Position.of("e4"), new Rook(Color.BLACK));
        }});

        // when, then
        assertThatThrownBy(() -> chessBoard.move(Position.of("d4"), Position.of("f4")))
                .isInstanceOf(InvalidMoveStrategyException.class)
                .hasMessage("중간에 말이 있어 행마할 수 없습니다.");
    }

    @DisplayName("목적지에 같은 팀의 말이 있다면 예외를 발생시킨다.")
    @Test
    void throwExceptionWhenMoveToSameTeam() {
        // given
        this.chessBoard = ChessBoard.from(new HashMap<Position, Piece>(){{
            put(Position.of("d4"), new Rook(Color.WHITE));
            put(Position.of("e4"), new Rook(Color.WHITE));
        }});

        // when, then
        assertThatThrownBy(() -> chessBoard.move(Position.of("d4"), Position.of("e4")))
                .isInstanceOf(InvalidMoveStrategyException.class)
                .hasMessage("동일한 진영의 말이 있어서 행마할 수 없습니다.");
    }
}