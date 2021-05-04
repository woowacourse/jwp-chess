package chess.dto.response;

import chess.dto.ChessGameDto;
import chess.dto.RoomDto;

public class ChessRoomStatusResponse {
    RoomDto room;
    ChessGameDto chessGame;

    public ChessRoomStatusResponse(final RoomDto room, final ChessGameDto chessGame) {
        this.room = room;
        this.chessGame = chessGame;
    }

    public RoomDto getRoom() {
        return room;
    }

    public ChessGameDto getChessGame() {
        return chessGame;
    }
}
