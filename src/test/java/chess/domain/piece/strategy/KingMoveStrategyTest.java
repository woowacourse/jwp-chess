package chess.domain.piece.strategy;

import chess.domain.board.ChessBoard;
import chess.domain.exception.InvalidMoveStrategyException;
import chess.domain.piece.Color;
import chess.domain.piece.King;
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

class KingMoveStrategyTest {
    private ChessBoard chessBoard;

    @DisplayName("킹의 이동 가능한 경우 테스트")
    @ParameterizedTest(name="{2}")
    @CsvSource({
            "d4, d5, NORTH",
            "d4, d3, SOUTH",
            "d4, e4, EAST",
            "d4, c4, WEST",
            "d4, c3, SOUTHWEST",
            "d4, e3, SOUTHEAST",
            "d4, c5, NORTHWEST",
            "d4, e5, NORTHEAST"})
    void kingCanMoveTest(String from, String to, String testCaseDescription) {
        // given
        this.chessBoard = ChessBoard.from(new HashMap<Position, Piece>(){{
            put(Position.of("d4"), new King(Color.WHITE));
        }});

        Piece pieceToMove = this.chessBoard.getPieceByPosition(Position.of(from));

        // when
        this.chessBoard.move(Position.of(from), Position.of(to));

        // then
        Piece pieceWhichHasBeenMoved = this.chessBoard.getPieceByPosition(Position.of(to));
        assertThat(pieceWhichHasBeenMoved).isEqualTo(pieceToMove);
    }

    @DisplayName("킹이 2칸 이상 움직이면 예외를 발생한다.")
    @Test
    void whenBlockedThrowTest() {
        this.chessBoard = ChessBoard.from(new HashMap<Position, Piece>(){{
            put(Position.of("d4"), new King(Color.WHITE));
        }});
        assertThatThrownBy(() -> chessBoard.move(Position.of("d4"), Position.of("f6")))
                .isInstanceOf(InvalidMoveStrategyException.class)
                .hasMessage("킹이 움직일 수 있는 범위를 벗어났습니다.");
    }

    @DisplayName("목적지에 같은 팀의 말이 있다면 예외를 발생한다.")
    @Test
    void throwExceptionWhenMoveToSameTeam() {
        this.chessBoard = ChessBoard.from(new HashMap<Position, Piece>(){{
            put(Position.of("d4"), new King(Color.WHITE));
            put(Position.of("d5"), new Pawn(Color.WHITE));
        }});

        assertThatThrownBy(() -> chessBoard.move(Position.of("d4"), Position.of("d5")))
                .isInstanceOf(InvalidMoveStrategyException.class)
                .hasMessage("동일한 진영의 말이 있어서 행마할 수 없습니다.");
    }
}