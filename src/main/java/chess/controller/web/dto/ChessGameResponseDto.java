package chess.controller.web.dto;

import chess.chessgame.domain.manager.ChessGameManager;
import chess.chessgame.domain.piece.attribute.Color;

import java.util.Map;

public class ChessGameResponseDto {
    private final long gameId;
    private final String color;
    private final Map<String, PieceDto> piecesAndPositions;

    public ChessGameResponseDto(long gameId, Color color, Map<String, PieceDto> piecesAndPositions) {
        this.gameId = gameId;
        this.color = color.name();
        this.piecesAndPositions = piecesAndPositions;
    }

    public ChessGameResponseDto(ChessGameManager chessGameManager) {
        this.gameId = chessGameManager.getId();
        this.color = chessGameManager.nextColor().name();
        this.piecesAndPositions = chessGameManager.getPieces();
    }

    public long getGameId() {
        return gameId;
    }

    public String getColor() {
        return color;
    }

    public Map<String, PieceDto> getPiecesAndPositions() {
        return piecesAndPositions;
    }
}