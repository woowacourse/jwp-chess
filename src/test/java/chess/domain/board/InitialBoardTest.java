package chess.domain.board;

import static chess.domain.board.coordinate.Column.*;
import static chess.domain.board.coordinate.Row.*;
import static chess.domain.piece.Symbol.*;
import static chess.domain.piece.Team.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import chess.domain.board.coordinate.Column;
import chess.domain.board.coordinate.Coordinate;
import chess.domain.board.coordinate.Row;
import chess.domain.piece.Piece;

class InitialBoardTest {

    private Map<Coordinate, Piece> map = InitialBoard.initialize();

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
                Arguments.arguments(Arrays.asList(A, H),
                        Arrays.asList(EIGHT), Piece.of(ROOK.name(), BLACK.name())),
                Arguments.arguments(Arrays.asList(A, H),
                        Arrays.asList(ONE), Piece.of(ROOK.name(), WHITE.name())),
                Arguments.arguments(Arrays.asList(B, G),
                        Arrays.asList(EIGHT), Piece.of(KNIGHT.name(), BLACK.name())),
                Arguments.arguments(Arrays.asList(B, G),
                        Arrays.asList(ONE), Piece.of(KNIGHT.name(), WHITE.name())),
                Arguments.arguments(Arrays.asList(C, F),
                        Arrays.asList(EIGHT), Piece.of(BISHOP.name(), BLACK.name())),
                Arguments.arguments(Arrays.asList(C, F),
                        Arrays.asList(ONE), Piece.of(BISHOP.name(), WHITE.name())),
                Arguments.arguments(Arrays.asList(D),
                        Arrays.asList(EIGHT), Piece.of(QUEEN.name(), BLACK.name())),
                Arguments.arguments(Arrays.asList(D),
                        Arrays.asList(ONE), Piece.of(QUEEN.name(), WHITE.name())),
                Arguments.arguments(Arrays.asList(E),
                        Arrays.asList(EIGHT), Piece.of(KING.name(), BLACK.name())),
                Arguments.arguments(Arrays.asList(E),
                        Arrays.asList(ONE), Piece.of(KING.name(), WHITE.name())),
                Arguments.arguments(Arrays.asList(Column.values()),
                        Arrays.asList(SEVEN), Piece.of(PAWN.name(), BLACK.name())),
                Arguments.arguments(Arrays.asList(Column.values()),
                        Arrays.asList(TWO), Piece.of(PAWN.name(), WHITE.name())),
                Arguments.arguments(Arrays.asList(Column.values()),
                        Arrays.asList(THREE, FOUR, FIVE, SIX), Piece.of(EMPTY.name(), NONE.name()))
        );
    }
}

