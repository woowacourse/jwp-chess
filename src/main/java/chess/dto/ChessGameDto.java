package chess.dto;

import chess.domain.ChessGame;
import chess.domain.ChessMap;
import chess.domain.player.Team;

import java.util.Map;

public class ChessGameDto {

    private final long roomId;
    private final String roomName;
    private final Map<String, Character> chessMap;
    private final String turn;
    private final boolean isRunning;

    public ChessGameDto(long roomId, String roomName, Map<String, Character> chessMap, String turn, boolean isRunning) {
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

    public long getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public Map<String, Character> getChessMap() {
        return chessMap;
    }

    public String getTurn() {
        return turn;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
