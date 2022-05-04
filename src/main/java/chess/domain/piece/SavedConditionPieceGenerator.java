package chess.domain.piece;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public enum SavedConditionPieceGenerator {
    KING("KING"::equals, (color, moveCount) -> new King(Color.find(color), moveCount)),
    QUEEN("QUEEN"::equals, (color, moveCount) -> new Queen(Color.find(color), moveCount)),
    ROOK("ROOK"::equals, (color, moveCount) -> new Rook(Color.find(color), moveCount)),
    BISHOP("BISHOP"::equals, (color, moveCount) -> new Bishop(Color.find(color), moveCount)),
    KNIGHT("KNIGHT"::equals, (color, moveCount) -> new Knight(Color.find(color), moveCount)),
    PAWN("PAWN"::equals, (color, moveCount) -> new Pawn(Color.find(color), moveCount)),
    NONE("NONE"::equals, (color, moveCount) -> new None(Color.find(color), moveCount));

    private final Predicate<String> condition;
    private final BiFunction<String, Integer, Piece> colorOf;

    SavedConditionPieceGenerator(Predicate<String> condition, BiFunction<String, Integer, Piece> colorOf) {
        this.condition = condition;
        this.colorOf = colorOf;
    }

    public static Piece generatePiece(String type, String color, int moveCount) {
        return Arrays.stream(SavedConditionPieceGenerator.values())
                .filter(piece -> piece.condition.test(type))
                .map(piece -> piece.colorOf.apply(color, moveCount))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
