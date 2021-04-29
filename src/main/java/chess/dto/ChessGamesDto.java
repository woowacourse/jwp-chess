package chess.dto;

import java.util.List;

public class ChessGamesDto {
    private final List<ChessGameDto> roomList;

    public ChessGamesDto(List<ChessGameDto> roomList) {
        this.roomList = roomList;
    }

    public List<ChessGameDto> getRoomList() {
        return roomList;
    }
}
