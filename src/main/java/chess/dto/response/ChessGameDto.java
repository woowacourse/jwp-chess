package chess.dto.response;

import chess.domain.game.GameId;
import chess.domain.piece.PieceColor;

public class ChessGameDto {
    private final String gameId;
    private final PieceColorDto currentTurn;

    public ChessGameDto(String gameId, PieceColorDto currentTurn) {
        this.gameId = gameId;
        this.currentTurn = currentTurn;
    }

    public static ChessGameDto of(GameId gameId, String turn) {
        return new ChessGameDto(gameId.getGameId(), PieceColorDto.from(turn));
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
