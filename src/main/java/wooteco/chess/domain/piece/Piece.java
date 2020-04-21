package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.Path;
import wooteco.chess.domain.board.Position;

public abstract class Piece {
    Team team;
    double score;

    public Piece(Team team, double score) {
        this.team = team;
        this.score = score;
    }

    public abstract boolean isMovable(Path path);

    public abstract boolean isInitialPosition(final Position position);

    public boolean isEnemy(Piece piece) {
        if (team.equals(piece.team)) {
            return false;
        }
        return true;
    }

    public boolean isTeamOf(Team team) {
        return this.team.equals(team);
    }
}
