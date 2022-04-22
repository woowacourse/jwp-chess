package chess.domain.piece;

import java.util.Collections;
import java.util.List;

import chess.domain.piece.detail.Direction;
import chess.domain.piece.detail.PieceType;
import chess.domain.piece.detail.Team;
import chess.domain.square.Square;

public class Blank extends Piece {

    public Blank(final Team team, final Square square) {
        super(team, square);
        validateTeam(team);
    }

    private void validateTeam(final Team team) {
        if (!team.isNone()) {
            throw new IllegalArgumentException("빈칸은 팀을 가질 수 없습니다.");
        }
    }

    @Override
    public Piece moveTo(final Square to) {
        throw new IllegalStateException("빈칸은 이동할 수 없습니다.");
    }

    @Override
    public boolean isBlank() {
        return true;
    }

    @Override
    public boolean isMovable(final Piece piece) {
        return false;
    }

    @Override
    public List<Direction> getAvailableDirections() {
        return Collections.emptyList();
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.BLANK;
    }
}
