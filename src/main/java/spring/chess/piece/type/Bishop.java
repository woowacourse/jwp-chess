package spring.chess.piece.type;

import spring.chess.piece.type.movable.BishopMovable;
import spring.chess.score.Score;
import spring.chess.team.Team;

public class Bishop extends Piece {
    private static final char NAME = 'b';
    private static final Score SCORE = new Score(3);

    public Bishop(Team team) {
        super(NAME, SCORE, team, new BishopMovable());
    }
}

