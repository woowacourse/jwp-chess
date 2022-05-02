package chess.web.dto;

public class RoomDto {
    private int roomId;
    private String roomTitle;
    private boolean finished;
    private boolean deleted;

    public RoomDto() {
    }

    public RoomDto(int roomId, String roomTitle, boolean finished, boolean deleted) {
        this.roomId = roomId;
        this.roomTitle = roomTitle;
        this.finished = finished;
        this.deleted = deleted;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
