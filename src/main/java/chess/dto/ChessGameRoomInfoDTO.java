package chess.dto;

public final class ChessGameRoomInfoDTO {

    private final String id;
    private final String name;
    private final String password;

    public ChessGameRoomInfoDTO(final String id, final String name, final String password) {
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

    @Override
    public String toString() {
        return "ChessGameDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
