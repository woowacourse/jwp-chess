package chess.domain.entity;

public class BoardPiece {

    private String boardPieceId;
    private String gameId;
    private String position;
    private String piece;

    public BoardPiece(String boardPieceId, String gameId, String position, String piece) {
        this.boardPieceId = boardPieceId;
        this.gameId = gameId;
        this.position = position;
        this.piece = piece;
    }

    public String getGameId() {
        return gameId;
    }

    public String getPosition() {
        return position;
    }

    public String getPiece() {
        return piece;
    }
}
