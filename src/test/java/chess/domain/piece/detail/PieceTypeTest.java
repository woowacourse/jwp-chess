package chess.domain.piece.detail;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PieceTypeTest {

    @ParameterizedTest
    @CsvSource({"P,PAWN", "N,KNIGHT", "B,BISHOP", "R,ROOK", "Q,QUEEN", "K,KING"})
    void getPieceType(final String symbol, final String rawPieceType) {
        PieceType pieceType = PieceType.valueOf(rawPieceType);
        assertThat(pieceType.getSymbol()).isEqualTo(symbol);
    }
}
