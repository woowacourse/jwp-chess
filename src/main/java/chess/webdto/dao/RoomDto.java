package chess.webdto.dao;

public class RoomDto {
    private long roomId;
    private String turn;
    private boolean isPlaying;
    private String name;

    public RoomDto() {
    }

    public RoomDto(long roomId, String turn, boolean isPlaying, String name) {
        this.roomId = roomId;
        this.turn = turn;
        this.isPlaying = isPlaying;
        this.name = name;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public boolean getIsPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(boolean playing) {
        isPlaying = playing;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
