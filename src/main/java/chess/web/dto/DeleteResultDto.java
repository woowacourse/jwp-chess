package chess.web.dto;

public class DeleteResultDto {
    private int roomId;
    private boolean deleted;

    public DeleteResultDto() {
    }

    public DeleteResultDto(int roomId, boolean deleted) {
        this.roomId = roomId;
        this.deleted = deleted;
    }

    public int getRoomId() {
        return roomId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
