package chess.dto;

import chess.domain.MovingPosition;
import chess.domain.Position;

public class MovingPositionDto {
    private final int fromX;
    private final int fromY;
    private final int toX;
    private final int toY;

    public MovingPositionDto(MovingPosition movingPosition){
        Position from = movingPosition.getFrom();
        Position to = movingPosition.getTo();

        fromX = from.getX();
        fromY = from.getY();

        toX = to.getX();
        toY = to.getY();
    }

    public int getFromX() {
        return fromX;
    }

    public int getFromY() {
        return fromY;
    }

    public int getToX() {
        return toX;
    }

    public int getToY() {
        return toY;
    }
}
