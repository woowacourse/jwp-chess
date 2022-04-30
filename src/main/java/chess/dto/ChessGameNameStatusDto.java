package chess.dto;

public class ChessGameNameStatusDto {

    private final int id;
    private final String name;
    private final boolean running;

    public ChessGameNameStatusDto(int id, String name, boolean running) {
        this.id = id;
        this.name = name;
        this.running = running;
    }

    public ChessGameNameStatusDto(int id, String name) {
        this(id, name, true);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isRunning() {
        return running;
    }
}
