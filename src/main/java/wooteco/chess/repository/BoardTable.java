package wooteco.chess.repository;

public class BoardTable {

    private String position;
    private String piece;
    private String team;

    public BoardTable(String position, String piece, String team) {
        this.position = position;
        this.piece = piece;
        this.team = team;
    }

    public String getPosition() {
        return position;
    }

    public String getPiece() {
        return piece;
    }

    public String getTeam() {
        return team;
    }
}
