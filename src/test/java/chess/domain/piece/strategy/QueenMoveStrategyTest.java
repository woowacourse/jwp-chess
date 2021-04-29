package chess.domain.piece.strategy;

import chess.domain.board.ChessBoard;
import chess.domain.exception.InvalidMoveStrategyException;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.Queen;
import chess.domain.position.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QueenMoveStrategyTest {
    private ChessBoard chessBoard;

    @DisplayName("퀸의 이동 가능한 경우 테스트")
    @ParameterizedTest(name="{2}")
    @CsvSource({
            "d4, d6, NORTH",
            "d4, d2, SOUTH",
            "d4, f4, EAST",
            "d4, b4, WEST",
            "d4, b2, SOUTHWEST",
            "d4, f2, SOUTHEAST",
            "d4, b6, NORTHWEST",
            "d4, f6, NORTHEAST"})
    void queenCanMoveTest(String from, String to, String testCaseDescription) {
        // given
        this.chessBoard = ChessBoard.from(new HashMap<Position, Piece>(){{
            put(Position.of("d4"), new Queen(Color.WHITE));
        }});

        Piece pieceToMove = this.chessBoard.getPieceByPosition(Position.of(from));

        // when
        this.chessBoard.move(Position.of(from), Position.of(to));

        // then
        Piece pieceWhichHasBeenMoved = this.chessBoard.getPieceByPosition(Position.of(to));
        assertThat(pieceWhichHasBeenMoved).isEqualTo(pieceToMove);
    }

    @DisplayName("움직일 수 없는 방향으로 이동하려고 한다면 예외를 발생시킨다.")
    @ParameterizedTest(name="{2}")
    @CsvSource({
            "d4, e6, NNE",
            "d4, c6, NNW",
            "d4, e2, SSE",
            "d4, c2, SSW",
            "d4, f5, EEN",
            "d4, f3, EEW",
            "d4, b5, WWN",
            "d4, b3, WWS"})
    void throwExceptionWhenWrongDirection(String from, String to) {
        // given
        this.chessBoard = ChessBoard.from(new HashMap<Position, Piece>(){{
            put(Position.of("d4"), new Queen(Color.WHITE));
        }});

        // when, then
        assertThatThrownBy(() -> chessBoard.move(Position.of(from), Position.of(to)))
                .isInstanceOf(InvalidMoveStrategyException.class)
                .hasMessage("움직일 수 없는 방향입니다.");
    }

    @DisplayName("기물이 가는 길에 다른 기물이 있으면 예외")
    @Test
    void whenBlockedThrowTest() {
        // given
        this.chessBoard = ChessBoard.from(new HashMap<Position, Piece>(){{
            put(Position.of("d4"), new Queen(Color.WHITE));
            put(Position.of("e4"), new Queen(Color.BLACK));
        }});

        // when, then
        assertThatThrownBy(() -> chessBoard.move(Position.of("d4"), Position.of("f4")))
                .isInstanceOf(InvalidMoveStrategyException.class)
                .hasMessage("중간에 말이 있어 행마할 수 없습니다.");
    }

    @DisplayName("목적지에 같은 팀의 말이 있다면 예외")
    @Test
    void throwExceptionWhenMoveToSameTeam() {
        // given
        this.chessBoard = ChessBoard.from(new HashMap<Position, Piece>(){{
            put(Position.of("d4"), new Queen(Color.WHITE));
            put(Position.of("e4"), new Queen(Color.WHITE));
        }});

        // when, then
        assertThatThrownBy(() -> chessBoard.move(Position.of("d4"), Position.of("e4")))
                .isInstanceOf(InvalidMoveStrategyException.class)
                .hasMessage("동일한 진영의 말이 있어서 행마할 수 없습니다.");
    }
}
