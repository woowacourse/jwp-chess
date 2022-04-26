package chess.controller.dto.request;

import chess.domain.ChessGameRoom;

public class ChessGameRoomCreateRequest {

    private String title;
    private String password;

    private ChessGameRoomCreateRequest() {
    }

    public ChessGameRoomCreateRequest(String title, String password) {
        this.title = title;
        this.password = password;
    }

    public ChessGameRoom toChessGameRoom() {
        return new ChessGameRoom(title, password);
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }
}
