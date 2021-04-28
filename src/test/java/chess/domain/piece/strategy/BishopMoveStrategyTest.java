package chess.domain.piece.strategy;

import chess.domain.board.ChessBoard;
import chess.domain.exception.InvalidMoveStrategyException;
import chess.domain.piece.Bishop;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BishopMoveStrategyTest {
    private ChessBoard chessBoard;

    @BeforeEach
    void setUp() {
        this.chessBoard = ChessBoard.from(new HashMap<Position, Piece>(){{
            put(Position.of("d4"), new Bishop(Color.WHITE));
            put(Position.of("g7"), new Bishop(Color.BLACK));    // 중간 행마길에 존재하는 장애물
            put(Position.of("a1"), new Bishop(Color.WHITE));    // 목적지의 아군
        }});
    }

    @DisplayName("비숍의 이동 가능한 방향 테스트")
    @CsvSource({"d4, b2, SOUTHWEST", "d4, f2, SOUTHEAST", "d4, b6, NORTHWEST", "d4, f6, NORTHEAST"})
    @ParameterizedTest(name="{2}")
    void bishopCanMoveTest(String from, String to, String testCaseDescription) {
        // given
        Piece pieceToMove = this.chessBoard.getPieceByPosition(Position.of(from));

        // when
        this.chessBoard.move(Position.of(from), Position.of(to));

        // then
        Piece pieceWhichHasBeenMoved = this.chessBoard.getPieceByPosition(Position.of(to));
        assertThat(pieceWhichHasBeenMoved).isEqualTo(pieceToMove);
    }

    @DisplayName("직선 방향으로 이동하려고 한다면 예외를 발생시킨다.")
    @ParameterizedTest
    @CsvSource({"d4, d5", "d4, e4"})
    void throwExceptionWhenWrongDirection(String from, String to) {
        assertThatThrownBy(() -> chessBoard.move(Position.of(from), Position.of(to)))
                .isInstanceOf(InvalidMoveStrategyException.class)
                .hasMessage("움직일 수 없는 방향입니다.");
    }

    @DisplayName("기물이 가는 길에 다른 기물이 있으면 예외를 발생시킨다.")
    @ParameterizedTest
    @CsvSource({"d4, h8"})
    void whenBlockedThrowTest(String from, String to) {
        assertThatThrownBy(() -> chessBoard.move(Position.of(from), Position.of(to)))
                .isInstanceOf(InvalidMoveStrategyException.class)
                .hasMessage("중간에 말이 있어 행마할 수 없습니다.");
    }

    @DisplayName("목적지에 같은 팀의 말이 있다면 예외를 발생시킨다.")
    @ParameterizedTest
    @CsvSource({"d4, a1"})
    void throwExceptionWhenMoveToSameTeam(String from, String to) {
        assertThatThrownBy(() -> chessBoard.move(Position.of(from), Position.of(to)))
                .isInstanceOf(InvalidMoveStrategyException.class)
                .hasMessage("동일한 진영의 말이 있어서 행마할 수 없습니다.");
    }
}
