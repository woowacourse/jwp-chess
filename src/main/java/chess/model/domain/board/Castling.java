package chess.model.domain.board;

import chess.model.domain.state.MoveInfo;
import java.util.Set;
import java.util.stream.Collectors;

public class Castling {

    private final Set<CastlingSetting> castling;

    private Castling(Set<CastlingSetting> castling) {
        this.castling = castling;
    }

    public static Castling createInitial() {
        return new Castling(CastlingSetting.getCastling());
    }

    public static Castling of(Set<CastlingSetting> castling) {
        return new Castling(castling);
    }

    public boolean canCastling(MoveInfo moveInfo) {
        return CastlingSetting.canCastling(castling, moveInfo);
    }

    public boolean remove(Square moveInfoBefore) {
        return castling.removeAll(castling.stream()
            .filter(element -> element.isEqualSquare(moveInfoBefore))
            .collect(Collectors.toSet()));
    }

    public Set<CastlingSetting> getCastling() {
        return castling;
    }
}
