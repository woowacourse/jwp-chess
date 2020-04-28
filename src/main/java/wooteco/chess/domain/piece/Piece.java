package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.Path;
import wooteco.chess.domain.board.Position;

public abstract class Piece {

    protected final Team team;
    private final String name;
    private final double score;

    public Piece(final String name, final Team team, final double score) {
        this.name = name;
        this.team = team;
        this.score = score;
    }

    public static Piece of(final String pieceIdentifier) {
        return PieceRepository.pieces()
                .stream()
                .filter(piece -> piece.is(pieceIdentifier))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 피스가 존재하지 않습니다."));
    }

    private boolean is(String identifier) {
        return toString().equals(identifier);
    }

    public boolean isEnemy(final Piece piece) {
        return !team.equals(piece.team);
    }

    public boolean isTeamOf(final Team team) {
        if (this.team == null) {
            throw new IllegalArgumentException("유효하지 않은 팀입니다.");
        }
        return this.team.equals(team);
    }

    public double getScore() {
        return score;
    }

    public abstract boolean isMovable(Path path);

    public abstract boolean isInitialPosition(final Position position);

    @Override
    public String toString() {
        return team.toString() + name;
    }
}
