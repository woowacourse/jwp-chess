package chess.domain.gameRoom.dto;

public final class ChessGameRoomShowInfoDTO {

    private final String id;
    private final String name;

    public ChessGameRoomShowInfoDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return "ChessGameDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
