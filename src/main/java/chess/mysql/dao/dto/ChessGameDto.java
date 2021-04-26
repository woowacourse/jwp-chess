package chess.mysql.dao.dto;

import chess.chessgame.domain.room.game.ChessGameManager;
import chess.converter.PiecesConverter;

public class ChessGameDto {
    private final long id;
    private final String nextTurn;
    private final boolean running;
    private final String pieces;

    public ChessGameDto(ChessGameManager chessGameManager) {
        this.id = chessGameManager.getId();
        this.nextTurn = chessGameManager.nextColor().name();
        this.running = chessGameManager.isStart() && chessGameManager.isNotEnd();
        this.pieces = PiecesConverter.convertString(chessGameManager.getBoard());
    }

    public ChessGameDto(long id, String nextTurn, boolean running, String pieces) {
        this.id = id;
        this.nextTurn = nextTurn;
        this.running = running;
        this.pieces = pieces;
    }

    public Long getId() {
        return id;
    }

    public String getNextTurn() {
        return nextTurn;
    }

    public boolean isRunning() {
        return running;
    }

    public String getPieces() {
        return pieces;
    }
}
