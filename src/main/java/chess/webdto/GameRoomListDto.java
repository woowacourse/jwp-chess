package chess.webdto;

import java.util.List;

public class GameRoomListDto {
    private final List<GameRoomDto> gameRooms;

    public GameRoomListDto(List<GameRoomDto> gameRooms) {
        this.gameRooms = gameRooms;
    }

    public List<GameRoomDto> getGameRooms() {
        return gameRooms;
    }
}
