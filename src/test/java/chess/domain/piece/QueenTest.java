package chess.domain.piece;

import chess.domain.chessboard.Board;
import chess.domain.chessboard.position.File;
import chess.domain.chessboard.position.Rank;
import chess.domain.piece.Piece;
import chess.domain.piece.Queen;
import chess.domain.piece.Symbol;
import chess.domain.game.Player;
import chess.domain.chessboard.position.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class QueenTest {

    private Board board;

    @BeforeEach
    void init() {
        board = new Board();
    }

    @DisplayName("target 위치로 움직일 수 없으면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"SEVEN,D", "ONE,D", "SEVEN,G", "SEVEN,A", "ONE,A", "ONE,G"})
    void canMove_false(final Rank rank, final File file) {
        final Map<Position, Piece> chessBoard = board.getBoard();
        final Piece queen = new Queen(Player.BLACK, Symbol.QUEEN);
        final boolean actual = queen.canMove(Position.of(Rank.FOUR, File.D), Position.of(rank, file), chessBoard);

        assertThat(actual).isFalse();
    }

    @DisplayName("target 위치로 움직일 수 있으면 true를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"SIX,F", "SIX,B", "TWO,B", "TWO,B", "FOUR,H", "FOUR,A", "TWO,D", "SIX,D"})
    void canMove_true(final Rank rank, final File file) {
        final Map<Position, Piece> chessBoard = board.getBoard();
        final Piece queen = new Queen(Player.BLACK, Symbol.QUEEN);
        final boolean actual = queen.canMove(Position.of(Rank.FOUR, File.D), Position.of(rank, file), chessBoard);

        assertThat(actual).isTrue();
    }
}
