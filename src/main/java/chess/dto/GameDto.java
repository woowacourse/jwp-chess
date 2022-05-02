package chess.dto;

import chess.entity.Game;

public class GameDto {

    private int id;
    private String title;
    private String state;

    private GameDto(int id, String title, String state) {
        this.id = id;
        this.title = title;
        this.state = state;
    }

    public static GameDto of(Game game) {
        return new GameDto(game.getId(), game.getTitle(), game.getState());
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getState() {
        return state;
    }
}
