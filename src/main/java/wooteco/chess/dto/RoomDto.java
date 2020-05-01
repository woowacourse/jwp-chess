package wooteco.chess.dto;

import java.util.Objects;

public class RoomDto {
    public static final Long DEFAULT_ROOM_ID = -1L;
    public static final Long DEFAULT_USER_ID = -1L;
    public static final String DEFAULT_NAME = "default";

    private Long roomId;
    private String name;
    private Long blackUserId;
    private Long whiteUserId;
    private boolean isEnd;

    public RoomDto(Long roomId, Long blackUserId, Long whiteUserId, boolean isEnd, String name) {
        this.roomId = roomId;
        this.blackUserId = blackUserId;
        this.whiteUserId = whiteUserId;
        this.isEnd = isEnd;
        this.name = name;
    }

    public RoomDto() {
        this(DEFAULT_ROOM_ID, DEFAULT_USER_ID, DEFAULT_USER_ID, false, DEFAULT_NAME);
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBlackUserId() {
        return blackUserId;
    }

    public void setBlackUserId(Long blackUserId) {
        this.blackUserId = blackUserId;
    }

    public Long getWhiteUserId() {
        return whiteUserId;
    }

    public void setWhiteUserId(Long whiteUserId) {
        this.whiteUserId = whiteUserId;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomDto roomDto = (RoomDto) o;
        return roomId == roomDto.roomId &&
                blackUserId == roomDto.blackUserId &&
                whiteUserId == roomDto.whiteUserId &&
                isEnd == roomDto.isEnd &&
                Objects.equals(name, roomDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId, name, blackUserId, whiteUserId, isEnd);
    }
}
