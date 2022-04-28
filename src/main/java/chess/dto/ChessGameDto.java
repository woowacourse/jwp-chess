package chess.dto;

import chess.domain.ChessGame;
import chess.domain.ChessMap;
import chess.domain.player.Team;

public class ChessGameDto {

    private final String roomName;
    private final char[][] chessMap;
    private final String turn;
    private final boolean isRunning;

    private ChessGameDto(String roomName, char[][] chessMap, String turn, boolean isRunning) {
        this.roomName = roomName;
        this.chessMap = chessMap;
        this.turn = turn;
        this.isRunning = isRunning;
    }

    public static ChessGameDto of(final ChessGame chessGame, final String roomName) {
        final ChessMap chessMap = chessGame.createMap();
        final Team turn = chessGame.getTurn();
        final boolean isRunning = chessGame.isRunning();
        return new ChessGameDto(roomName, chessMap.getChessMap(), turn.getName(), isRunning);
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
