package chess.domain.piece.property;

public enum Team {

    BLACK,
    WHITE;

    public Team changeTeam() {
        if (this == WHITE){
            return BLACK;
        }
        return WHITE;
    }
}
