package chess.dto;

public class RoomDto {

    private final String id;
    private final String name;
    private final String full;

    public RoomDto(String id, String name, boolean full) {
        this.id = id;
        this.name = name;
        this.full = convertToString(full);
    }

    private String convertToString(boolean isFull) {
        if (isFull) {
            return "(2/2)";
        }
        return "(1/2)";
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getFull() {
        return full;
    }
}
