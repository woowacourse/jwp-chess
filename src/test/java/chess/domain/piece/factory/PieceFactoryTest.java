package chess.domain.piece.factory;

import static chess.domain.piece.PieceTeam.BLACK;
import static chess.domain.piece.PieceTeam.EMPTY;
import static chess.domain.piece.PieceTeam.WHITE;

import chess.domain.piece.EmptySpace;
import chess.domain.piece.King;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PieceFactoryTest {

    @DisplayName("piece를 생성할 수 있다")
    @ParameterizedTest
    @MethodSource("supplyPieceTestCase")
    void can_create_piece(String input, Piece expectedPiece) {
        Piece createdPiece = PieceFactory.create(input);
        Assertions.assertThat(createdPiece).isEqualTo(expectedPiece);
    }

    private static Stream<Arguments> supplyPieceTestCase() {
        return Stream.of(
                Arguments.of("k", new King(WHITE)),
                Arguments.of("whiteKing", new King(WHITE)),

                Arguments.of("K", new King(BLACK)),
                Arguments.of("blackKing", new King(BLACK)),

                Arguments.of("p", new Pawn(WHITE)),
                Arguments.of("whitePawn", new Pawn(WHITE)),

                Arguments.of("P", new Pawn(BLACK)),
                Arguments.of("blackPawn", new Pawn(BLACK)),

                Arguments.of(".", new EmptySpace(EMPTY))
        );
    }
}