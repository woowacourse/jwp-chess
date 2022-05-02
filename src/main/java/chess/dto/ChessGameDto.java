package chess.dto;

import chess.model.ChessGame;

public class ChessGameDto {

    private Long id;
    private String title;
    private String password;

    public ChessGameDto(String title, String password) {
        this.title = title;
        this.password = password;
    }

    public ChessGameDto(ChessGame chessGame) {
        this.id = chessGame.getId();
        this.title = chessGame.getTitle();
        this.password = chessGame.getPassword();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }
}
