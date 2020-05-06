package wooteco.chess.dto.response;

import wooteco.chess.repository.ChessRoom;

import java.util.List;

public class StartResponse {
    private List<ChessRoom> chessRooms;

    public StartResponse(List<ChessRoom> chessRooms) {
        this.chessRooms = chessRooms;
    }

    public List<ChessRoom> getChessRooms() {
        return chessRooms;
    }
}
