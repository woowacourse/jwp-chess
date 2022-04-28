package chess.entities;

import chess.domain.pieces.Color;
import chess.domain.pieces.Type;

public class ChessPiece {

    private final Integer id;
    private final Color color;
    private final Type type;
    private final Integer positionId;

    public ChessPiece(Integer id, Color color, Type type, Integer positionId) {
        this.id = id;
        this.color = color;
        this.type = type;
        this.positionId = positionId;
    }

    public ChessPiece(Color color, Type type, Integer positionId) {
        this(null, color, type, positionId);
    }

    public Integer getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }

    public Type getType() {
        return type;
    }

    public Integer getPositionId() {
        return positionId;
    }
}
