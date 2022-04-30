package chess.dao;

import chess.domain.Color;

public class FakeBoard {

    private final Color turn;
    private final String name;
    private final String password;

    public FakeBoard(Color turn, String name, String password) {
        this.turn = turn;
        this.name = name;
        this.password = password;
    }

    public Color getTurn() {
        return turn;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
