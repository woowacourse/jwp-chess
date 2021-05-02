package chess.util;

import chess.domain.piece.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PieceConverterTest {
    @Test
    void convert_test() {
        final String kingName = PieceConverter.convertToPieceName("black", new King());
        assertThat(kingName).isEqualTo("K");

        final String queenName = PieceConverter.convertToPieceName("white", new Queen());
        assertThat(queenName).isEqualTo("Q");

        final String rookName = PieceConverter.convertToPieceName("black", new Rook());
        assertThat(rookName).isEqualTo("R");

        final String knightName = PieceConverter.convertToPieceName("white", new Knight());
        assertThat(knightName).isEqualTo("N");

        final String bishopName = PieceConverter.convertToPieceName("black",new Bishop());
        assertThat(bishopName).isEqualTo("B");

        final String blackPawnName = PieceConverter.convertToPieceName("black", new Pawn(-1));
        assertThat(blackPawnName).isEqualTo("P");

        final String whitePawnName = PieceConverter.convertToPieceName("white", new Pawn(1));
        assertThat(whitePawnName).isEqualTo("p");
    }
}
