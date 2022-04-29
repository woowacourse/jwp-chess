package chess.domain.gameRoom.dto;

public class ChessGameRoomPassInfoDTO {
    private final String id;
    private final String name;
    private final String password;

    public ChessGameRoomPassInfoDTO(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}