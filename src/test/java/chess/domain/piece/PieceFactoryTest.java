package chess.domain.piece;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import chess.domain.piece.detail.PieceType;
import chess.domain.piece.detail.Team;
import chess.domain.square.Square;

class PieceFactoryTest {

    @DisplayName("기물 타입에 따른 기물을 반환한다.")
    @ParameterizedTest
    @CsvSource({"PAWN,WHITE", "ROOK,WHITE", "KNIGHT,WHITE", "BISHOP,WHITE", "QUEEN,WHITE", "KING,WHITE", "BLANK,NONE"})
    void createPiece(final String rawPieceType, final String rawTeam) {
        final PieceType pieceType = PieceType.valueOf(rawPieceType);
        final Team team = Team.valueOf(rawTeam);
        final Square square = Square.from("a1");
        final Piece piece = PieceFactory.createPiece(pieceType, team, square);

        assertThat(piece.getPieceType()).isSameAs(pieceType);
    }
}
