package chess.webdto;

public class GameRoomDto {
    private final int game_id;
    private final String room_name;

    public GameRoomDto(int game_id, String room_name) {
        this.game_id = game_id;
        this.room_name = room_name;
    }

    public int getGame_id() {
        return game_id;
    }

    public String getRoom_name() {
        return room_name;
    }
}
