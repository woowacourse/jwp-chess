package chess.domain;

import static chess.domain.Color.WHITE;
import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.piece.Piece;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PieceConverterTest {

    @Test
    @DisplayName("이름과 color를 받아 Piece를 생성")
    void parseToPiece() {
        Piece piece = PieceConverter.parseToPiece("king", WHITE);
        assertThat(piece).isInstanceOf(Piece.class);
    }
}
