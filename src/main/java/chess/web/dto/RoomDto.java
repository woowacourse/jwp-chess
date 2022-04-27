package chess.web.dto;

public class RoomDto {
    private int roomId;
    private String roomTitle;
    private boolean finished;

    public RoomDto() {
    }
    public RoomDto(int roomId, String roomTitle, boolean finished) {
        this.roomId = roomId;
        this.roomTitle = roomTitle;
        this.finished = finished;
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

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
