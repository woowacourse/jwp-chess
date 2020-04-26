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

class KnightTest {
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

    @DisplayName("Knight 클래스가 PieceScore에 있는 Enum Knight 동일한지 테스트")
    @Test
    void isSameNameTest() {
        Knight knight = new Knight(Color.WHITE, "n");
        assertThat(knight.isSameName(PieceScore.KNIGHT)).isTrue();
    }

    @DisplayName("Knight이 대각선으로 가는지 테스트")
    @Test
    void goTest() {
        Knight knight = new Knight(Color.WHITE, "n");
        pieces.addPiece(Position.of("b2"), knight);

        gameManager.moveFromTo(Position.of("b2"), Position.of("d3"));

        assertThat(pieces.getPieceByPosition(Position.of("d3"))).isEqualTo(knight);
    }

    @DisplayName("Knight이 못 가는 곳 테스트")
    @Test
    void goExceptionTest() {
        Knight knight = new Knight(Color.WHITE, "n");
        pieces.addPiece(Position.of("b2"), knight);

        assertThatThrownBy(() -> {
            gameManager.moveFromTo(Position.of("b2"), Position.of("h2"));
        }).isInstanceOf(IllegalArgumentException.class);
    }
}