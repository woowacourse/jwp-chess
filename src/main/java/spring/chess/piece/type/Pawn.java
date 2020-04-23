package spring.chess.piece.type;

import spring.chess.piece.type.movable.PawnPieceMovable;
import spring.chess.score.Score;
import spring.chess.team.Team;

public class Pawn extends Piece {
    private static final char NAME = 'p';
    private static final Score SCORE = new Score(1);

    public Pawn(Team team) {
        super(NAME, SCORE, team, new PawnPieceMovable(team));
    }
}
