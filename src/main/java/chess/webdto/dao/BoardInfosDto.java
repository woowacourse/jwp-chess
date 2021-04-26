package chess.webdto.dao;

public class BoardInfosDto {
    private String team;
    private String position;
    private String piece;
    private boolean isFirstMoved;

    public BoardInfosDto() {
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

    public void setIsFirstMoved(boolean isFirstMoved) {
        this.isFirstMoved = isFirstMoved;
    }

    public boolean isWhite() {
        return "white".equals(team);
    }

    public boolean isBlack() {
        return "black".equals(team);
    }
}
