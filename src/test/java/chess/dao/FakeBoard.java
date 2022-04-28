package chess.dao;

public class FakeBoard {

    private final String turn;
    private final String name;
    private final String password;

    public FakeBoard(String turn, String name, String password) {
        this.turn = turn;
        this.name = name;
        this.password = password;
    }

    public String getTurn() {
        return turn;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
