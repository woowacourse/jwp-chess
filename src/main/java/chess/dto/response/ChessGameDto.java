package chess.dto.response;

import chess.domain.piece.PieceColor;

public class ChessGameDto {
    private final int gameId;
    private final PieceColorDto currentTurn;

    public ChessGameDto(int gameId, PieceColorDto currentTurn) {
        this.gameId = gameId;
        this.currentTurn = currentTurn;
    }

    public static ChessGameDto of(int gameId, String turn) {
        return new ChessGameDto(gameId, PieceColorDto.from(turn));
    }

    public PieceColor getCurrentTurnAsPieceColor() {
        return currentTurn.getPieceColor();
    }

    @Override
    public String toString() {
        return "ChessGameDto{" +
            "gameId='" + gameId + '\'' +
            ", currentTurn=" + currentTurn +
            '}';
    }
}
