package chess.dto;

public class RoomLoadResponse {
    RoomDto roomDto;
    ChessGameDto chessGameDto;

    public RoomLoadResponse(final RoomDto roomDto, final ChessGameDto chessGameDto) {
        this.roomDto = roomDto;
        this.chessGameDto = chessGameDto;
    }

    public RoomDto getRoomDto() {
        return roomDto;
    }

    public ChessGameDto getChessGameDto() {
        return chessGameDto;
    }
}
