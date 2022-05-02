package chess.dto.response;

public class RoomDto {
    private final int id;
    private final String name;

    public RoomDto(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "RoomDto{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }
}
