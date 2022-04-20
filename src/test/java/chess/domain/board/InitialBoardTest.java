package chess.domain.board;

import static chess.domain.ChessFixtures.BISHOP_BLACK;
import static chess.domain.ChessFixtures.BISHOP_WHITE;
import static chess.domain.ChessFixtures.EMPTY_PIECE;
import static chess.domain.ChessFixtures.KING_BLACK;
import static chess.domain.ChessFixtures.KING_WHITE;
import static chess.domain.ChessFixtures.KNIGHT_BLACK;
import static chess.domain.ChessFixtures.KNIGHT_WHITE;
import static chess.domain.ChessFixtures.PAWN_BLACK;
import static chess.domain.ChessFixtures.PAWN_WHITE;
import static chess.domain.ChessFixtures.QUEEN_BLACK;
import static chess.domain.ChessFixtures.QUEEN_WHITE;
import static chess.domain.ChessFixtures.ROOK_BLACK;
import static chess.domain.ChessFixtures.ROOK_WHITE;
import static chess.domain.board.coordinate.Column.A;
import static chess.domain.board.coordinate.Column.B;
import static chess.domain.board.coordinate.Column.C;
import static chess.domain.board.coordinate.Column.D;
import static chess.domain.board.coordinate.Column.E;
import static chess.domain.board.coordinate.Column.F;
import static chess.domain.board.coordinate.Column.G;
import static chess.domain.board.coordinate.Column.H;
import static chess.domain.board.coordinate.Row.EIGHT;
import static chess.domain.board.coordinate.Row.FIVE;
import static chess.domain.board.coordinate.Row.FOUR;
import static chess.domain.board.coordinate.Row.ONE;
import static chess.domain.board.coordinate.Row.SEVEN;
import static chess.domain.board.coordinate.Row.SIX;
import static chess.domain.board.coordinate.Row.THREE;
import static chess.domain.board.coordinate.Row.TWO;
import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.board.coordinate.Column;
import chess.domain.board.coordinate.Coordinate;
import chess.domain.board.coordinate.Row;
import chess.domain.piece.Piece;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class InitialBoardTest {

    private final Map<Coordinate, Piece> map = InitialBoard.initialize();

    @ParameterizedTest(name = "{0}{1}에 {2}가 위치한다.")
    @MethodSource("provideParameters")
    void initialize(List<Column> columns, List<Row> rows, Piece piece) {
        for (Column column : columns) {
            checkPieceByCoordinate(rows, piece, column);
        }
    }

    private void checkPieceByCoordinate(List<Row> rows, Piece piece, Column column) {
        for (Row row : rows) {
            assertThat(map.get(Coordinate.of(column, row))).isEqualTo(piece);
        }
    }

    private static Stream<Arguments> provideParameters() {
        return Stream.of(
                Arguments.arguments(List.of(A, H), List.of(EIGHT), ROOK_BLACK),
                Arguments.arguments(List.of(A, H), List.of(ONE), ROOK_WHITE),
                Arguments.arguments(List.of(B, G), List.of(EIGHT), KNIGHT_BLACK),
                Arguments.arguments(List.of(B, G), List.of(ONE), KNIGHT_WHITE),
                Arguments.arguments(List.of(C, F), List.of(EIGHT), BISHOP_BLACK),
                Arguments.arguments(List.of(C, F), List.of(ONE), BISHOP_WHITE),
                Arguments.arguments(List.of(D), List.of(EIGHT), QUEEN_BLACK),
                Arguments.arguments(List.of(D), List.of(ONE), QUEEN_WHITE),
                Arguments.arguments(List.of(E), List.of(EIGHT), KING_BLACK),
                Arguments.arguments(List.of(E), List.of(ONE), KING_WHITE),
                Arguments.arguments(List.of(Column.values()), List.of(SEVEN), PAWN_BLACK),
                Arguments.arguments(List.of(Column.values()), List.of(TWO), PAWN_WHITE),
                Arguments.arguments(List.of(Column.values()), List.of(THREE, FOUR, FIVE, SIX), EMPTY_PIECE)
        );
    }
}

