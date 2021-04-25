package chess.webdto.dao;

public class TeamInfoDto {
    private String team;
    private String position;
    private String piece;
    private boolean isFirstMoved;
    private long roomId;

    public TeamInfoDto() {
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPiece() {
        return piece;
    }

    public void setPiece(String piece) {
        this.piece = piece;
    }

    public boolean getIsFirstMoved() {
        return isFirstMoved;
    }

    public void setIsFirstMoved(boolean firstMoved) {
        isFirstMoved = firstMoved;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }


}
