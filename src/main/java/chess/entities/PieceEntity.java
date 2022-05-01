package chess.entities;

import chess.domain.pieces.Color;
import chess.domain.pieces.Type;

public class PieceEntity {

    private final Integer id;
    private final Color color;
    private final Type type;
    private final Integer positionId;

    public PieceEntity(Integer id, Color color, Type type, Integer positionId) {
        this.id = id;
        this.color = color;
        this.type = type;
        this.positionId = positionId;
    }

    public PieceEntity(Color color, Type type, Integer positionId) {
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
