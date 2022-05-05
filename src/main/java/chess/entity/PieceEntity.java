package chess.entity;

public class PieceEntity {
    private static final int TRASH_INT = -1;
    private static final String TRASH_STRING = "";
    private final int pieceId;
    private final int gameId;
    private final String pieceName;
    private final String pieceColor;
    private final String position;

    private PieceEntity(int pieceId, int gameId, String pieceName, String pieceColor, String position) {
        this.pieceId = pieceId;
        this.gameId = gameId;
        this.pieceName = pieceName;
        this.pieceColor = pieceColor;
        this.position = position;
    }

    public static PieceEntity of(int gameId) {
        return new PieceEntity(TRASH_INT, gameId, TRASH_STRING, TRASH_STRING, TRASH_STRING);
    }

    public static PieceEntity of(int gameId, String position) {
        return new PieceEntity(TRASH_INT, gameId, TRASH_STRING, TRASH_STRING, position);
    }

    public static PieceEntity of(int gameId, String pieceName, String pieceColor, String position) {
        return new PieceEntity(TRASH_INT, gameId, pieceName, pieceColor, position);
    }

    public static PieceEntity of(int pieceId, int gameId, String pieceName, String pieceColor, String position) {
        return new PieceEntity(pieceId, gameId, pieceName, pieceColor, position);
    }

    public int getPieceId() {
        return pieceId;
    }

    public int getGameId() {
        return gameId;
    }

    public String getPieceName() {
        return pieceName;
    }

    public String getPieceColor() {
        return pieceColor;
    }

    public String getPosition() {
        return position;
    }
}
