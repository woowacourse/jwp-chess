package chess.controller.dto.response;

import chess.domain.ChessGameRoom;

public class ChessGameRoomTitleResponse {

    private long id;
    private String title;

    private ChessGameRoomTitleResponse() {
    }

    private ChessGameRoomTitleResponse(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public static ChessGameRoomTitleResponse from(ChessGameRoom chessGameRoom) {
        return new ChessGameRoomTitleResponse(chessGameRoom.getId(), chessGameRoom.getTitle());
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
