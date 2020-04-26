package wooteco.chess.domain.piece;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.chess.domain.Color;
import wooteco.chess.domain.GameManager;
import wooteco.chess.domain.PieceScore;
import wooteco.chess.domain.board.Position;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RookTest {
    Map<Position, Piece> originPiece;
    Pieces pieces;
    GameManager gameManager;

    @BeforeEach
    void setUp() {
        originPiece = new HashMap<>();
        PiecesFactory.createBlankPieces(originPiece);
        pieces = new Pieces(originPiece);
        gameManager = new GameManager(pieces, Color.WHITE);
    }

    @DisplayName("Rook 클래스가 PieceScore에 있는 Enum Rook 동일한지 테스트")
    @Test
    void isSameNameTest() {
        Rook rook = new Rook(Color.WHITE, "n");
        assertThat(rook.isSameName(PieceScore.ROOK)).isTrue();
    }

    @DisplayName("Rook이 대각선으로 가는지 테스트")
    @Test
    void goTest() {
        Rook rook = new Rook(Color.WHITE, "n");
        pieces.addPiece(Position.of("b2"), rook);

        gameManager.moveFromTo(Position.of("b2"), Position.of("h2"));

        assertThat(pieces.getPieceByPosition(Position.of("h2"))).isEqualTo(rook);
    }

    @DisplayName("Rook이 못 가는 곳 테스트")
    @Test
    void goExceptionTest() {
        Rook rook = new Rook(Color.WHITE, "n");
        pieces.addPiece(Position.of("b2"), rook);

        assertThatThrownBy(() -> {
            gameManager.moveFromTo(Position.of("b2"), Position.of("h3"));
        }).isInstanceOf(IllegalArgumentException.class);
    }
}