package chess.dto.response;

public class ChessGameStatusDto {

    private final int id;
    private final String name;
    private final String turn;
    private final boolean running;

    public ChessGameStatusDto(int id, String name, String turn, boolean running) {
        this.id = id;
        this.name = name;
        this.turn = turn;
        this.running = running;
    }

    public ChessGameStatusDto(int id, String name, String turn) {
        this(id, name, turn, true);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTurn() {
        return turn;
    }

    public boolean isRunning() {
        return running;
    }
}
