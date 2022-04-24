package chess.dto;

import chess.domain.piece.Piece;
import chess.domain.position.Position;

public final class BoardElementDto {

    private int gameId;
    private String pieceName;
    private String pieceColor;
    private String position;

    public BoardElementDto(int gameId, Position position, Piece piece) {
        this.gameId = gameId;
        this.pieceName = piece.getNotation().name();
        this.pieceColor = piece.getColor().name();
        this.position = position.getFile().name() + position.getRankNumber();
    }

    public BoardElementDto(int gameId, String pieceName, String pieceColor, String position) {
        this.gameId = gameId;
        this.pieceName = pieceName;
        this.pieceColor = pieceColor;
        this.position = position;
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
