package chess.domain;

import chess.domain.password.EmptyPassword;
import chess.domain.password.Password;
import chess.domain.password.WhitePassword;

public class Room {

    private Game game;
    private Password whitePassword;
    private Password blackPassword;

    public Room(Game game, String whitePassword) {
        this.game = game;
        this.whitePassword = new WhitePassword(whitePassword);
        this.blackPassword = new EmptyPassword();
    }

    public Game getGame() {
        return game;
    }
}
