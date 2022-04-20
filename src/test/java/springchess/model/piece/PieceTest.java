package springchess.model.piece;

import chess.model.piece.Empty;
import chess.model.piece.Piece;
import chess.model.piece.Rook;
import chess.model.piece.Team;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PieceTest {

    @Test
    void createPiece() {
        chess.model.piece.Piece rook = new chess.model.piece.Rook(chess.model.piece.Team.WHITE);

        assertThat(rook).isInstanceOf(chess.model.piece.Piece.class);
    }

    @Test
    void isBlack() {
        chess.model.piece.Piece blackPiece = new chess.model.piece.Rook(chess.model.piece.Team.BLACK);
        chess.model.piece.Piece whitePiece = new Rook(Team.WHITE);
        Piece nothing = new Empty();

        assertAll(
                () -> assertThat(blackPiece.isBlack()).isTrue(),
                () -> assertThat(whitePiece.isBlack()).isFalse(),
                () -> assertThat(nothing.isBlack()).isFalse()
        );
    }
}
