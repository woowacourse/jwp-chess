package spring.chess.piece.type;

import spring.chess.piece.type.movable.QueenPieceMovable;
import spring.chess.score.Score;
import spring.chess.team.Team;

public class Queen extends Piece {
    private static final char NAME = 'q';
    private static final Score SCORE = new Score(9);

    public Queen(Team team) {
        super(NAME, SCORE, team, new QueenPieceMovable());
    }
}
