package wooteco.chess.dto;

import wooteco.chess.domain.coordinate.Coordinate;

import java.util.Objects;

public class MoveDto {
    public static final Long DEFAULT_MOVE_ID = -1L;
    private Long moveId;
    private Long roomId;
    private Coordinate source;
    private Coordinate target;

    public MoveDto(Long moveId, Long roomId, Coordinate source, Coordinate target) {
        this.moveId = moveId;
        this.roomId = roomId;
        this.source = source;
        this.target = target;
    }

    public MoveDto(Long roomId, Coordinate source, Coordinate target) {
        this(DEFAULT_MOVE_ID, roomId, source, target);
    }

    public Long getMoveId() {
        return moveId;
    }

    public void setMoveId(Long moveId) {
        this.moveId = moveId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Coordinate getSource() {
        return source;
    }

    public void setSource(Coordinate source) {
        this.source = source;
    }

    public Coordinate getTarget() {
        return target;
    }

    public void setTarget(Coordinate target) {
        this.target = target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveDto moveDto = (MoveDto) o;
        return moveId == moveDto.moveId &&
                roomId == moveDto.roomId &&
                Objects.equals(source, moveDto.source) &&
                Objects.equals(target, moveDto.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moveId, roomId, source, target);
    }
}
