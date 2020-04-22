package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.Path;
import wooteco.chess.domain.board.Position;

public abstract class Piece {
    String name;
    Team team;
    double score;

    public Piece(String name, Team team, double score) {
        this.name = name;
        this.team = team;
        this.score = score;
    }

    public static Piece of(String pieceName) {
        return PieceRepository.pieces()
                .stream()
                .filter(piece -> piece.toString().equals(pieceName))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 피스가 존재하지 않습니다."));
    }

    public abstract boolean isMovable(Path path);

    public abstract boolean isInitialPosition(final Position position);

    public boolean isEnemy(Piece piece) {
        return !team.equals(piece.team);
    }

    public boolean isTeamOf(Team team) {
        if (this.team == null) {
            throw new IllegalArgumentException("유효하지 않은 팀입니다.");
        }
        return this.team.equals(team);
    }

    public double getScore() {
        return score;
    }

    public String toString() {
        return team.toString() + name;
    }
}
