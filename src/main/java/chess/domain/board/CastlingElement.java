package chess.domain.board;

import chess.domain.state.MoveInfo;
import java.util.Set;
import java.util.stream.Collectors;

public class CastlingElement {

    private final Set<CastlingSetting> castlingElements;

    private CastlingElement(Set<CastlingSetting> castlingElements) {
        this.castlingElements = castlingElements;
    }

    public static CastlingElement createInitial() {
        return new CastlingElement(CastlingSetting.getCastlingElements());
    }

    public static CastlingElement of(Set<CastlingSetting> castlingElements) {
        return new CastlingElement(castlingElements);
    }

    public boolean canCastling(MoveInfo moveInfo) {
        return CastlingSetting.canCastling(castlingElements, moveInfo);
    }

    public boolean remove(Square moveInfoBefore) {
        return castlingElements.removeAll(castlingElements.stream()
            .filter(element -> element.isEqualSquare(moveInfoBefore))
            .collect(Collectors.toSet()));
    }

    public Set<CastlingSetting> getCastlingElements() {
        return castlingElements;
    }
}
