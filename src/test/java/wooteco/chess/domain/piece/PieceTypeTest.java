package wooteco.chess.domain.piece;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class PieceTypeTest {
    @DisplayName("체스 말 각각의 점수 확인")
    @ParameterizedTest
    @MethodSource("getCasesForPieceScore")
    void pieceScore(PieceType pieceType, double expectedScore) {
        assertThat(pieceType.getScore()).isEqualTo(expectedScore);
    }

    private static Stream<Arguments> getCasesForPieceScore() {
        return Stream.of(
                Arguments.of(PieceType.FIRST_WHITE_PAWN, 1),
                Arguments.of(PieceType.FIRST_BLACK_PAWN, 1),
                Arguments.of(PieceType.WHITE_PAWN, 1),
                Arguments.of(PieceType.BLACK_PAWN, 1),
                Arguments.of(PieceType.WHITE_ROOK, 5),
                Arguments.of(PieceType.BLACK_ROOK, 5),
                Arguments.of(PieceType.WHITE_KNIGHT, 2.5),
                Arguments.of(PieceType.BLACK_KNIGHT, 2.5),
                Arguments.of(PieceType.WHITE_BISHOP, 3),
                Arguments.of(PieceType.BLACK_BISHOP, 3),
                Arguments.of(PieceType.WHITE_QUEEN, 9),
                Arguments.of(PieceType.BLACK_QUEEN, 9),
                Arguments.of(PieceType.WHITE_KING, 0),
                Arguments.of(PieceType.BLACK_KING, 0)
        );
    }
}
