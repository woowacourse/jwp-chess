package chess.model.piece;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PieceTest {

    @Test
    void createPiece() {
        Piece rook = new Rook(Team.WHITE);

        assertThat(rook).isInstanceOf(Piece.class);
    }

    @Test
    void isBlack() {
        Piece blackPiece = new Rook(Team.BLACK);
        Piece whitePiece = new Rook(Team.WHITE);
        Piece nothing = new Empty();

        assertAll(
                () -> assertThat(blackPiece.isBlack()).isTrue(),
                () -> assertThat(whitePiece.isBlack()).isFalse(),
                () -> assertThat(nothing.isBlack()).isFalse()
        );
    }
}
