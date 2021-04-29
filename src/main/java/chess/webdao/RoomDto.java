package chess.webdao;

public class RoomDto {
    private long roomId;
    private String turn;
    private boolean isPlaying;
    private String name;
    private String password;

    public RoomDto(){
    }
    public RoomDto(long roomId, String turn, boolean isPlaying, String name, String password) {
        this.roomId = roomId;
        this.turn = turn;
        this.isPlaying = isPlaying;
        this.name = name;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
