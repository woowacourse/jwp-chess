package chess.dto.web;

public class RoomStatusDto {
    public String status;

    public RoomStatusDto() {
    }

    public RoomStatusDto(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
