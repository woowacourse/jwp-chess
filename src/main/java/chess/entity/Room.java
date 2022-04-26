package chess.entity;

public class Room {

    private final int id;
    private final String name;
    private final String password;
    private final String gameStatus;
    private final String currentTurn;

    public Room(final int id, final String name, final String password, final String gameStatus,
                final String currentTurn) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.gameStatus = gameStatus;
        this.currentTurn = currentTurn;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public String getCurrentTurn() {
        return currentTurn;
    }
}
