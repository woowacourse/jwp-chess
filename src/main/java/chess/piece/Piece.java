package chess.piece;

import chess.domain.MovingPosition;
import chess.domain.Position;

import java.util.List;

public abstract class Piece {
    private final Type type;
    protected final Color color;

    public Piece(Type type, Color color) {
        this.type = type;
        this.color = color;
    }

    public abstract boolean isMovable(MovingPosition movingPosition);

    public abstract List<Position> computeMiddlePosition(MovingPosition movingPosition);

    public boolean isSameType(Type type) {
        return this.type == type;
    }

    public boolean isSameColor(Color color) {
        return this.color == color;
    }

    public String getSymbolByColor() {
        return type.getSymbol(color);
    }

    public double getScore() {
        return type.getScore();
    }

    public Color getColor() {
        return color;
    }

    public Type getType() {
        return type;
    }

    public String getColorToString(){
        return color.getColor();
    }

    public String getTypeToString(){
        return type.getSymbol();
    }
}
