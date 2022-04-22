package chess.domain;

import chess.domain.piece.PieceRule;
import chess.domain.piece.multiple.Bishop;
import chess.domain.piece.multiple.Queen;
import chess.domain.piece.multiple.Rook;
import chess.domain.piece.single.Knight;
import java.util.Arrays;
import java.util.Objects;

public enum PromotionPiece {

    QUEEN("Q", new Queen()),
    ROOK("R", new Rook()),
    BISHOP("B", new Bishop()),
    KNIGHT("N", new Knight()),
    ;

    private final String value;
    private final PieceRule pieceRule;

    PromotionPiece(String value, PieceRule pieceRule) {
        this.value = value;
        this.pieceRule = pieceRule;
    }

    public static PromotionPiece createPromotionPiece(String input) {
        Objects.requireNonNull(input, "input은 null이 들어올 수 없습니다.");
        return Arrays.stream(values())
                .filter(promotion -> promotion.value.equals(input))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("불가능한 프로모션 기물 이름입니다."));
    }
}
