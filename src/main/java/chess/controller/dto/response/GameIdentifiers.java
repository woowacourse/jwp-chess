package chess.controller.dto.response;

public class GameIdentifiers {

    private final long id;
    private final String name;

    public GameIdentifiers(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
