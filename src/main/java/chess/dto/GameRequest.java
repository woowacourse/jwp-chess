package chess.dto;

import chess.domain.ChessGame;

public class GameRequest {
    private final String title;
    private final String password;

    public GameRequest(String title, String password) {
        this.title = title;
        this.password = password;
    }

    public ChessGame toChessGame() {
        return new ChessGame(title, password);
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }
}
