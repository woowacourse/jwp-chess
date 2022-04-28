package chess.dto;

import chess.domain.ChessGame;
import chess.domain.ChessMap;
import chess.domain.player.Team;

public class ChessGameDto {

    private final Long roomId;
    private final String roomName;
    private final char[][] chessMap;
    private final String turn;
    private final boolean isRunning;

    public ChessGameDto(Long roomId, String roomName, char[][] chessMap, String turn, boolean isRunning) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.chessMap = chessMap;
        this.turn = turn;
        this.isRunning = isRunning;
    }

    public static ChessGameDto of(final long roomId, final ChessGame chessGame, final String roomName) {
        final ChessMap chessMap = chessGame.createMap();
        final Team turn = chessGame.getTurn();
        final boolean isRunning = chessGame.isRunning();
        return new ChessGameDto(roomId, roomName, chessMap.getChessMap(), turn.getName(), isRunning);
    }

    public static ChessGameDto of(final long roomId, final ChessGame chessGame) {
        final ChessMap chessMap = chessGame.createMap();
        final Team turn = chessGame.getTurn();
        final boolean isRunning = chessGame.isRunning();
        return new ChessGameDto(roomId, null, chessMap.getChessMap(), turn.getName(), isRunning);
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public char[][] getChessMap() {
        return chessMap;
    }

    public String getTurn() {
        return turn;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
