package spring.chess.piece.type;

import spring.chess.piece.type.movable.KnightPieceMovable;
import spring.chess.score.Score;
import spring.chess.team.Team;

public class Knight extends Piece {
    private static final char NAME = 'n';
    private static final Score SCORE = new Score(2.5);

    public Knight(Team team) {
        super(NAME, SCORE, team, new KnightPieceMovable());
    }
}
