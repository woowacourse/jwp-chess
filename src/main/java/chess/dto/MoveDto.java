package chess.dto;

import org.jetbrains.annotations.NotNull;

public class MoveDto {

    private int roomId;

    @NotNull("이동 시작 위치는  필수입니다.")
    private String from;
    @NotNull("이동 도착 위치는 필수입니다.")
    private String to;

    public MoveDto() {
    }

    public MoveDto(int roomId, String from, String to) {
        this.roomId = roomId;
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getRoomId() {
        return roomId;
    }
}
