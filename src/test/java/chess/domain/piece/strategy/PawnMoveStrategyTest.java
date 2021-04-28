package chess.domain.piece.strategy;

import chess.domain.board.BoardFactory;
import chess.domain.board.ChessBoard;
import chess.domain.order.MoveRoute;
import chess.domain.piece.Color;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.domain.exception.InvalidMoveStrategyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PawnMoveStrategyTest {
    private ChessBoard chessBoard;

    @BeforeEach
    void setUp() {
        chessBoard = BoardFactory.createBoard();
    }

    @DisplayName("행마에 대한 검증 - 직선")
    @ParameterizedTest
    @CsvSource({"a2, a3, WHITE", "b7, b6, BLACK"})
    void canMove_StraightDirection(String from, String to, Color color) {
        Pawn pawn = new Pawn(color);
        MoveRoute moveRoute = chessBoard.createMoveRoute(Position.of(from), Position.of(to));
        assertThat(pawn.canMove(moveRoute)).isTrue();
    }

    @DisplayName("직선으로 3칸 이상 이동할 경우 예외를 발생시킨다.")
    @ParameterizedTest
    @CsvSource({"c2, c5, WHITE", "g7, g4, BLACK"})
    void throwExceptionWhenMoveOverThreeSquares(String from, String to, Color color) {
        Pawn pawn = new Pawn(color);
        MoveRoute moveRoute = chessBoard.createMoveRoute(Position.of(from), Position.of(to));
        assertThatThrownBy(() -> pawn.canMove(moveRoute))
                .isInstanceOf(InvalidMoveStrategyException.class)
                .hasMessage("폰이 움직일 수 있는 범위를 벗어났습니다.");
    }

    @DisplayName("폰은 상대 말을 잡을 때 대각선으로 움직일 수 있다.")
    @Test
    void pawnKillMoveTest() {
        this.chessBoard = new ChessBoard(new HashMap<Position, Piece>(){{
            put(Position.of("d4"), new Pawn(Color.WHITE));
            put(Position.of("e5"), new Pawn(Color.BLACK));
        }});

        Piece whitePawn = chessBoard.getPieceByPosition((Position.of("d4")));
        MoveRoute moveRoute = chessBoard.createMoveRoute(Position.of("d4"), Position.of("e5"));

        assertThat(whitePawn.canMove(moveRoute)).isTrue();
    }

    @DisplayName("폰은 첫 행마에 2칸을 움직일 수 있다.")
    @ParameterizedTest
    @CsvSource({"a2, a4, WHITE", "b7, b5, BLACK"})
    void pawnCanMove2RankOnFirstMove(String from, String to, Color color) {
        Pawn pawn = new Pawn(color);
        MoveRoute moveRoute = chessBoard.createMoveRoute(Position.of(from), Position.of(to));
        assertThat(pawn.canMove(moveRoute)).isTrue();
    }

    @DisplayName("폰은 첫 행마가 아니라면 2칸을 움직일 수 없다.")
    @Test
    void pawnCannotMove2RankAfterFirstMove() {
        chessBoard.move(chessBoard.createMoveRoute(Position.of("a2"), Position.of("a4")));

        assertThatThrownBy(() -> {
            chessBoard.move(chessBoard.createMoveRoute(Position.of("a4"), Position.of("a6")));
        }).isInstanceOf(InvalidMoveStrategyException.class);

    }
}