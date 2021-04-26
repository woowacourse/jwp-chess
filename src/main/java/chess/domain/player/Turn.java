package chess.domain.player;

import chess.domain.piece.Owner;

import java.util.Arrays;

public enum Turn {
    BLACK(Owner.BLACK),
    WHITE(Owner.WHITE);

    private final Owner owner;

    Turn(final Owner owner) {
        this.owner = owner;
    }

    public static Turn of(final String turn) {
        return Arrays.stream(values())
                .filter(value -> value.name().equals(turn))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 turn symbol 입력 : " + turn));
    }

    public Turn change() {
        if (this.equals(BLACK)) {
            return WHITE;
        }
        return BLACK;
    }

    public boolean is(final Owner mover) {
        return owner.isSameTeam(mover);
    }
}
