package chess.domain.piece.strategy;

import chess.domain.board.ChessBoard;
import chess.domain.exception.InvalidMoveStrategyException;
import chess.domain.piece.Color;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PawnMoveStrategyTest {
    private ChessBoard chessBoard;

    @DisplayName("백색 폰은 북쪽으로, 흑색 폰은 남쪽으로 행마할 수 있다.")
    @ParameterizedTest
    @CsvSource({
            "d4, d5, A white pawn can move NORTH",
            "e5, e4, A black pawn can move SOUTH"})
    void canMove_StraightDirection(String from, String to, String testCaseDescription) {
        // given
        this.chessBoard = ChessBoard.from(new HashMap<Position, Piece>() {{
            put(Position.of("d4"), new Pawn(Color.WHITE));
            put(Position.of("e5"), new Pawn(Color.BLACK));
        }});

        Piece pieceToMove = this.chessBoard.getPieceByPosition(Position.of(from));

        // when
        this.chessBoard.move(Position.of(from), Position.of(to));

        // then
        Piece pieceWhichHasBeenMoved = this.chessBoard.getPieceByPosition(Position.of(to));
        assertThat(pieceWhichHasBeenMoved).isEqualTo(pieceToMove);
    }

    @DisplayName("폰은 첫 행마에 2칸을 움직일 수 있다.")
    @ParameterizedTest
    @CsvSource({
            "a2, a4, A white pawn can move 2 square on first move",
            "b7, b5, A black pawn can move 2 square on first move"})
    void pawnCanMove2RankOnFirstMove(String from, String to, String testCaseDescription) {
        // given
        this.chessBoard = ChessBoard.from(new HashMap<Position, Piece>() {{
            put(Position.of("a2"), new Pawn(Color.WHITE));
            put(Position.of("b7"), new Pawn(Color.BLACK));
        }});

        Piece pieceToMove = this.chessBoard.getPieceByPosition(Position.of(from));

        // when
        this.chessBoard.move(Position.of(from), Position.of(to));

        // then
        Piece pieceWhichHasBeenMoved = this.chessBoard.getPieceByPosition(Position.of(to));
        assertThat(pieceWhichHasBeenMoved).isEqualTo(pieceToMove);
    }

    @DisplayName("폰은 첫 행마가 아니라면 2칸을 움직일 수 없다.")
    @Test
    void pawnCannotMove2RankAfterFirstMove() {
        // given
        this.chessBoard = ChessBoard.from(new HashMap<Position, Piece>() {{
            put(Position.of("a2"), new Pawn(Color.WHITE));
        }});

        this.chessBoard.move(Position.of("a2"), Position.of("a4"));

        // when, then
        assertThatThrownBy(() -> chessBoard.move(Position.of("a4"), Position.of("a6")))
                .isInstanceOf(InvalidMoveStrategyException.class);

    }

    @DisplayName("어떠한 경우에도 직선으로 3칸 이상 이동할 경우 예외를 발생시킨다.")
    @ParameterizedTest
    @CsvSource({
            "a2, a5",
            "b7, b4"})
    void throwExceptionWhenMoveOverThreeSquares(String from, String to) {
        // given
        this.chessBoard = ChessBoard.from(new HashMap<Position, Piece>() {{
            put(Position.of("a2"), new Pawn(Color.WHITE));
            put(Position.of("b7"), new Pawn(Color.BLACK));
        }});

        // when, then
        assertThatThrownBy(() -> this.chessBoard.move(Position.of(from), Position.of(to)))
                .isInstanceOf(InvalidMoveStrategyException.class);
    }

    @DisplayName("폰은 상대 말을 잡을 때 대각선으로 움직일 수 있다.")
    @Test
    void pawnKillMoveTest() {
        // gieven
        this.chessBoard = ChessBoard.from(new HashMap<Position, Piece>() {{
            put(Position.of("a2"), new Pawn(Color.WHITE));
            put(Position.of("b3"), new Pawn(Color.BLACK));
        }});

        Piece pieceToMove = this.chessBoard.getPieceByPosition(Position.of("a2"));

        // when
        this.chessBoard.move(Position.of("a2"), Position.of("b3"));

        // then
        Piece pieceWhichHasBeenMoved = this.chessBoard.getPieceByPosition(Position.of("b3"));
        assertThat(pieceWhichHasBeenMoved).isEqualTo(pieceToMove);
    }

}