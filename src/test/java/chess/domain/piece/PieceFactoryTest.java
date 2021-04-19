package chess.domain.piece;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PieceFactoryTest {

    @DisplayName("체스 기물들 정상 생성 확인")
    @Test
    void createPieces() {
        List<Piece> pieces = PieceFactory.createPieces();
        assertThat(pieces).containsAll(createPiecesForTest());
    }

    private List<Piece> createPiecesForTest() {
        return Arrays.asList(
            Piece.createRook(Color.WHITE, 0, 0),
            Piece.createKnight(Color.WHITE, 0, 1),
            Piece.createBishop(Color.WHITE, 0, 2),
            Piece.createQueen(Color.WHITE, 0, 3),
            Piece.createKing(Color.WHITE, 0, 4),
            Piece.createBishop(Color.WHITE, 0, 5),
            Piece.createKnight(Color.WHITE, 0, 6),
            Piece.createRook(Color.WHITE, 0, 7),
            Piece.createPawn(Color.WHITE, 1, 0),
            Piece.createPawn(Color.WHITE, 1, 1),
            Piece.createPawn(Color.WHITE, 1, 2),
            Piece.createPawn(Color.WHITE, 1, 3),
            Piece.createPawn(Color.WHITE, 1, 4),
            Piece.createPawn(Color.WHITE, 1, 5),
            Piece.createPawn(Color.WHITE, 1, 6),
            Piece.createPawn(Color.WHITE, 1, 7),

            Piece.createRook(Color.BLACK, 7, 0),
            Piece.createKnight(Color.BLACK, 7, 1),
            Piece.createBishop(Color.BLACK, 7, 2),
            Piece.createQueen(Color.BLACK, 7, 3),
            Piece.createKing(Color.BLACK, 7, 4),
            Piece.createBishop(Color.BLACK, 7, 5),
            Piece.createKnight(Color.BLACK, 7, 6),
            Piece.createRook(Color.BLACK, 7, 7),
            Piece.createPawn(Color.BLACK, 6, 0),
            Piece.createPawn(Color.BLACK, 6, 1),
            Piece.createPawn(Color.BLACK, 6, 2),
            Piece.createPawn(Color.BLACK, 6, 3),
            Piece.createPawn(Color.BLACK, 6, 4),
            Piece.createPawn(Color.BLACK, 6, 5),
            Piece.createPawn(Color.BLACK, 6, 6),
            Piece.createPawn(Color.BLACK, 6, 7)

        );
    }

}