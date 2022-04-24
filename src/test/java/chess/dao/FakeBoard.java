package chess.dao;

public class FakeBoard {

    private final String turn;

    public FakeBoard(String turn) {
        this.turn = turn;
    }

    public String getTurn() {
        return turn;
    }
}
