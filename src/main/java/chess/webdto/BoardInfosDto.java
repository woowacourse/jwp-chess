package chess.webdto;

public class BoardInfosDto {
    private String team;
    private String position;
    private String piece;
    private boolean isFirstMoved;

    public BoardInfosDto() {
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setPiece(String piece) {
        this.piece = piece;
    }

    public void setIsFirstMoved(boolean isFirstMoved) {
        this.isFirstMoved = isFirstMoved;
    }

    public String getTeam() {
        return team;
    }

    public String getPosition() {
        return position;
    }

    public String getPiece() {
        return piece;
    }

    public boolean getIsFirstMoved() {
        return isFirstMoved;
    }
}
