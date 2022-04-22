package chess.domain.piece;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static chess.domain.piece.PieceFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PieceFactoryTest {

    @DisplayName("기물의 이름, 색상 문자열을 Piece 자식 객체로 변환하는 기능")
    @ParameterizedTest
    @MethodSource("providePieceStringAndPieceObject")
    void of(final String pieceColor, final String pieceName, final Piece expected) {
        assertThat(PieceFactory.of(pieceName, pieceColor)).isEqualTo(expected);
    }

    public static Stream<Arguments> providePieceStringAndPieceObject() {
        return Stream.of(
                Arguments.of(
                        "WHITE", "PAWN", WHITE_PAWN.piece,
                        "WHITE", "ROOK", WHITE_ROOK.piece,
                        "WHITE", "BISHOP", WHITE_BISHOP.piece,
                        "WHITE", "KNIGHT", WHITE_KNIGHT.piece,
                        "WHITE", "QUEEN", WHITE_QUEEN.piece,
                        "WHITE", "KING", WHITE_KING.piece,
                        "BLACK", "PAWN", BLACK_PAWN.piece,
                        "BLACK", "ROOK", BLACK_ROOK.piece,
                        "BLACK", "BISHOP", BLACK_BISHOP.piece,
                        "BLACK", "KNIGHT", BLACK_KNIGHT.piece,
                        "BLACK", "QUEEN", BLACK_QUEEN.piece,
                        "BLACK", "KING", BLACK_KING.piece
                )
        );
    }
}
