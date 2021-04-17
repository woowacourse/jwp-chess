package chess.domain.piece;

import chess.domain.color.type.TeamColor;
import chess.domain.piece.type.Direction;
import chess.domain.piece.type.PieceType;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Objects;

public abstract class Piece {

    private final PieceType pieceType;
    private final TeamColor teamColor;
    private final double score;
    private final List<Direction> directions;

    public Piece(PieceType pieceType, TeamColor teamColor, double score, List<Direction> directions) {
        this.pieceType = pieceType;
        this.teamColor = teamColor;
        this.score = score;
        this.directions = directions;
    }

    public static Piece of(String pieceValue) {
        TeamColor teamColor = TeamColor.findByPieceValue(pieceValue);
        PieceType pieceType = PieceType.findByPieceValue(pieceValue);
        try {
            String className = PieceType.getClassNameByPieceType(pieceType);
            Class<Piece> pieceClass = (Class<Piece>) Class.forName(className);
            Constructor<?> constructor = pieceClass.getConstructor(TeamColor.class);
            return (Piece) constructor.newInstance(teamColor);
        } catch (Exception e) {
            throw new IllegalArgumentException("Piece 구현 객체 생성에 실패했습니다.");
        }
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public TeamColor getTeamColor() {
        return teamColor;
    }

    public double getScore() {
        return score;
    }

    public String getName() {
        return pieceType.getName(teamColor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Piece)) {
            return false;
        }
        Piece piece = (Piece) o;
        return pieceType == piece.pieceType && teamColor == piece.teamColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceType, teamColor);
    }

    public boolean isCorrectMoveDirection(Direction moveRequestDirection) {
        return directions.contains(moveRequestDirection);
    }
}
