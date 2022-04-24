package chess.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.piece.Piece;
import chess.domain.piece.detail.Team;
import chess.domain.piece.multiplemove.Rook;
import chess.domain.piece.singlemove.King;
import chess.domain.square.Square;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class BoardTest {

    @DisplayName("기물이 움직였는지 확인한다.")
    @ParameterizedTest
    @CsvSource({"a2,a3", "a2,a4", "b1, a3", "b1,c3", "h7,h6", "h7,h5", "g8,h6", "g8,f6"})
    void move(final String rawFrom, final String rawTo) {
        final Board board = new Board(BoardInitializer.create());
        final Square from = Square.from(rawFrom);
        final Square to = Square.from(rawTo);
        final Piece expected = board.getPieceAt(from);

        board.move(from, to);

        assertThat(board.getPieceAt(to)).isInstanceOf(expected.getClass());
    }

    @DisplayName("기물이 갈 수 없는 위치로 이동 명령시 예외가 발생한다.")
    @ParameterizedTest
    @CsvSource({"a2,b3", "a2,b4", "b1,b3"})
    void invalidPieceMove(final String rawFrom, final String rawTo) {
        final Board board = new Board(BoardInitializer.create());
        final Square from = Square.from(rawFrom);
        final Square to = Square.from(rawTo);

        assertThatThrownBy(() -> board.move(from, to))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 위치로 이동할 수 없습니다.");
    }

    @DisplayName("기물이 갈 수 없는 위치로 이동 명령시 예외가 발생한다.")
    @ParameterizedTest
    @CsvSource({"b1,d2", "a1,a2", "d1,c1", "c1,b2", "e1,d1"})
    void cannotMoveToAllySquare(final String rawFrom, final String rawTo) {
        final Board board = new Board(BoardInitializer.create());
        final Square from = Square.from(rawFrom);
        final Square to = Square.from(rawTo);

        assertThatThrownBy(() -> board.move(from, to))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("같은 팀 위치로는 이동할 수 없습니다.");
    }

    @DisplayName("기물이 이동할 경로가 가로막혀 있으면 예외가 발생한다.")
    @ParameterizedTest
    @CsvSource({
            "a1,a3", "c1,a3", "d1,b3", "f1,h3", "h1,h3",
            "a8,a6", "c8,a6", "d8,b6", "f8,h6", "h8,h6"
    })
    void validateRoute(final String rawFrom, final String rawTo) {
        final Board board = new Board(BoardInitializer.create());
        final Square from = Square.from(rawFrom);
        final Square to = Square.from(rawTo);

        assertThatThrownBy(() -> board.move(from, to))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이동 경로에 장애물이 있습니다.");
    }

    @Test
    void isKingDead() {
        final Map<Square, Piece> rawBoard = Map.of(
                Square.from("a1"), new King(Team.WHITE, Square.from("a1")),
                Square.from("a2"), new King(Team.BLACK, Square.from("a2"))
        );
        final Board board = new Board(new HashMap<>(rawBoard));

        board.move(Square.from("a1"), Square.from("a2"));
        assertThat(board.isKingDead()).isTrue();
    }

    @Test
    void getTeamWithAliveKing() {
        final Map<Square, Piece> rawBoard = Map.of(
                Square.from("a1"), new King(Team.WHITE, Square.from("a1")),
                Square.from("a2"), new King(Team.BLACK, Square.from("a2"))
        );
        final Board board = new Board(new HashMap<>(rawBoard));

        board.move(Square.from("a1"), Square.from("a2"));
        assertThat(board.getTeamWithAliveKing()).isEqualTo(Team.WHITE);
    }

    @Test
    void invalidGetTeamWhenNoKingExist() {
        final Map<Square, Piece> rawBoard = Map.of(
                Square.from("a1"), new Rook(Team.WHITE, Square.from("a1")),
                Square.from("a2"), new Rook(Team.BLACK, Square.from("a2"))
        );
        final Board board = new Board(new HashMap<>(rawBoard));

        board.move(Square.from("a1"), Square.from("a2"));
        assertThatThrownBy(board::getTeamWithAliveKing)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("보드에 킹이 존재하지 않습니다.");
    }
}
